package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jfinal.server.undertow.UndertowKit;
import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;
import com.litongjava.modules.dev.tools.file.sync.utils.JschUtils;
import com.litongjava.utils.log.LogUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 检测文件变动
 */
@Slf4j
public class FileSyncWatcher extends Thread {

  // protected int watchingInterval = 1000; // 1900 与 2000 相对灵敏
  protected int watchingInterval = 500;

  protected List<Path> watchingPaths;
  private WatchService watcher = null;
  private WatchKey watchKey;
  protected volatile boolean running = true;
  private SyncInfo syncInfo;

  public FileSyncWatcher(SyncInfo syncInfo) {
    setName("FileWatcher");
    // 避免在调用 deploymentManager.stop()、undertow.stop() 后退出 JVM
    setPriority(Thread.MAX_PRIORITY);
    this.syncInfo = syncInfo;
    this.watchingPaths = buildWatchingPaths();

  }

  protected List<Path> buildWatchingPaths() {
    Set<String> watchingDirSet = new HashSet<>();
    // log.info("syncInfo:{}",syncInfo);
    String[] classPathArray = syncInfo.getLocalPath().split(File.pathSeparator);
    for (String classPath : classPathArray) {
      buildDirs(new File(classPath.trim()), watchingDirSet);
    }

    List<String> dirList = new ArrayList<String>(watchingDirSet);
    Collections.sort(dirList);

    List<Path> pathList = new ArrayList<Path>(dirList.size());
    System.out.println("观察的目录有:");
    for (String dir : dirList) {
      System.out.println(dir);
      pathList.add(Paths.get(dir));
    }

    return pathList;
  }

  private void buildDirs(File file, Set<String> watchingDirSet) {
    if (file.isDirectory()) {
      watchingDirSet.add(file.getPath());

      File[] fileList = file.listFiles();
      for (File f : fileList) {
        buildDirs(f, watchingDirSet);
      }
    }
  }

  public void run() {
    try {
      doRun();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  protected void doRun() throws IOException {
    watcher = FileSystems.getDefault().newWatchService();
    log.info("获取到的文件观察器是:" + watcher);
    addShutdownHook(watcher);

    for (Path path : watchingPaths) {
      path.register(watcher,
          // 删除
          StandardWatchEventKinds.ENTRY_DELETE,
          // 修改
          StandardWatchEventKinds.ENTRY_MODIFY,
          // 创建
          StandardWatchEventKinds.ENTRY_CREATE);
    }

    while (running) {
      try {
        // watchKey = watcher.poll(watchingInterval, TimeUnit.MILLISECONDS); //
        // watcher.take(); 阻塞等待
        // 比较两种方式的灵敏性，或许 take() 方法更好，起码资源占用少，测试 windows 机器上的响应
        watchKey = watcher.take();

        if (watchKey == null) {
          // System.out.println(System.currentTimeMillis() / 1000);
          continue;
        }
      } catch (Throwable e) { // 控制台 ctrl + c 退出 JVM 时也将抛出异常
        running = false;
        if (e instanceof InterruptedException) { // 另一线程调用 hotSwapWatcher.interrupt() 抛此异常
          Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        break;
      }

      List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
      for (WatchEvent<?> event : watchEvents) {
        Kind<?> kind = event.kind();
        Object context = event.context();
        String fileName = context.toString();
        log.info("{}检测到文件修改{}", watcher.toString(), kind.toString() + "," + fileName);

        if (context instanceof Path) {
          Path dir = (Path) watchKey.watchable();
          Path fullPath = dir.resolve(fileName);
          File file = fullPath.toFile();

          /**
           * 如果是排除文件不理会
           */
          if (isExclude(file)) {
            continue;
          }

          /**
           * 除去log文件和文件夹都上传
           */
          if ("ENTRY_DELETE".equals(kind.toString())) {
            deleteRemoteFile(file, syncInfo);
          } else {
            upload(file, syncInfo);
          }
        }
      }
      resetWatchKey();
    }

  }

  private boolean isExclude(File file) {
    if (file.isDirectory()) {
      log.info("排除文件夹：{}",file.getName());
      return true;
    } else if (file.getName().endsWith("~")) {
      return true;
    }
    return false;
  }

  private void resetWatchKey() {
    if (watchKey != null) {
      watchKey.reset();
      watchKey = null;
    }
  }

  /**
   * 添加关闭钩子在 JVM 退出时关闭 WatchService
   * 
   * 注意：addShutdownHook 方式添加的回调在 kill -9 pid 强制退出 JVM 时不会被调用
   *      kill 不带参数 -9 时才回调
   */
  protected void addShutdownHook(WatchService watcher) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        watcher.close();
      } catch (Throwable e) {
        UndertowKit.doNothing(e);
      }
    }));
  }

  public void exit() {
    running = false;
    try {
      this.interrupt();
    } catch (Throwable e) {
      UndertowKit.doNothing(e);
    }
  }

  public void close() {
    try {
      log.info("关闭:{}", watcher);
      watcher.close();
    } catch (IOException e) {
      log.error(LogUtils.getStackTraceInfo(e));
    }
  }

  /**
   * 上传文件到远程服务器
   * @param file
   * @param syncInfo
   */
  public void upload(File file, SyncInfo syncInfo) {
    String localFilePath = file.getAbsolutePath();
    String remoteFilePath = JschUtils.getRemoteFullPath(localFilePath, syncInfo.getLocalPath(), syncInfo.getRemotePath());
    if (remoteFilePath == null) {
      log.info("没有匹配到路径:{}", localFilePath);
      return;
    }
    JschUtils.upload(localFilePath, remoteFilePath, syncInfo.getRemoteIp(), syncInfo.getRemotePort(), syncInfo.getRemoteUser(),
        syncInfo.getRemotePswd());

  }

  public void deleteRemoteFile(File file, SyncInfo syncInfo) {
    String localFilePath = file.getAbsolutePath();
    String remoteFilePath = JschUtils.getRemoteFullPath(localFilePath, syncInfo.getLocalPath(), syncInfo.getRemotePath());
    if (remoteFilePath == null) {
      log.info("没有匹配到路径:{}", localFilePath);
      return;
    }
    JschUtils.delete(remoteFilePath, syncInfo.getRemoteIp(), syncInfo.getRemotePort(), syncInfo.getRemoteUser(), syncInfo.getRemotePswd());
  }
}