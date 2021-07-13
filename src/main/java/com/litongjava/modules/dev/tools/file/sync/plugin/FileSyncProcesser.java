package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;

import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;
import com.litongjava.modules.dev.tools.file.sync.utils.JschUtils;
import com.litongjava.utils.file.PathUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月13日 上午12:18:32 
 * @version 1.0 
 * @desc
 */
@Slf4j
public class FileSyncProcesser {

  /**
   * 创建变动后的文件
   * @param kind
   * @param context
   * @param fileName
   * @param syncInfo 
   * @param file2 
   */
  public static void process(Kind<?> kind, Object context, String fileName, WatchKey watchKey, SyncInfo syncInfo) {
    if (context instanceof Path) {
      /**
       * 如果是排除文件不理会
       */
      Path dir = (Path) watchKey.watchable();
      Path fullPath = dir.resolve(fileName);
      File file = fullPath.toFile();
      if (isExclude(file)) {
        return;
      }
      
      if ("ENTRY_DELETE".equals(kind.toString())) { //删除文件
        ifDirectoryNotDelete(syncInfo, file);
      } else { //更新文件
        if (!file.exists()) { // 文件重命名为新的名字,删除远程的文件
          ifDirectoryNotDelete(syncInfo, file);
        } else {
          upload(file, syncInfo);
        }
      }
    }
  }

  /**
   * 如果是文件夹则不删除,只删除文件,删除远程文件夹,如果远程文件夹下面的有文件会出现异常,导致程序出现其他问题
   * @param syncInfo
   * @param file
   */
  private static void ifDirectoryNotDelete(SyncInfo syncInfo, File file) {
    if(PathUtils.isDirecotry(file.getAbsolutePath())) {
      //实际,测试删除远程目录会出现下面的错误4: Failure
      log.info("跳过对目录的删除:{}",file.getAbsolutePath());
    }else {
      deleteRemoteFile(file, syncInfo);
    }
  }

  /**
   * 排除不处理的文件
   * @param file
   * @return
   */
  public static boolean isExclude(File file) {
    String absolutePath = file.getAbsolutePath();
    if (file.isDirectory()) {
      log.info("排除文件夹:{}", file.getName());
      return true;
    } else if (file.getName().endsWith("~")) {
      log.info("排除文件：{}", file.getName());
      return true;
    } else if (absolutePath.contains("META-INF/maven")) { // 排除META-INF/maven目录
      log.info("排除文件：{}", absolutePath);
      return true;
    }
    return false;
  }

  /**
   * 上传文件到远程服务器
   * @param file
   * @param syncInfo
   */
  public static void upload(File file, SyncInfo syncInfo) {
    String localFilePath = file.getAbsolutePath();
    String remoteFilePath = JschUtils.getRemoteFullPath(localFilePath, syncInfo.getLocalPath(), syncInfo.getRemotePath());
    if (remoteFilePath == null) {
      log.info("没有匹配到路径:{}", localFilePath);
      return;
    }
    JschUtils.upload(localFilePath, remoteFilePath, syncInfo.getRemoteIp(), syncInfo.getRemotePort(), syncInfo.getRemoteUser(),
        syncInfo.getRemotePswd());

  }

  /**
   * 删除远程服务器上的文件
   * @param file
   * @param syncInfo
   */
  public static void deleteRemoteFile(File file, SyncInfo syncInfo) {
    String localFilePath = file.getAbsolutePath();
    String remoteFilePath = JschUtils.getRemoteFullPath(localFilePath, syncInfo.getLocalPath(), syncInfo.getRemotePath());
    if (remoteFilePath == null) {
      log.info("没有匹配到路径:{}", localFilePath);
      return;
    }
    JschUtils.delete(remoteFilePath, syncInfo.getRemoteIp(), syncInfo.getRemotePort(), syncInfo.getRemoteUser(), syncInfo.getRemotePswd());
  }

}
