package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.plugin.IPlugin;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午5:31:18 
 * @version 1.0 
 * @desc
 */
@Slf4j
public class WatchServicePlugin implements IPlugin {

  private Map<String,WathchServiceTaskInfo> taskInfoMap = new HashMap<>();
  @Override
  public boolean start() {
    for (Map.Entry<String, WathchServiceTaskInfo> e : taskInfoMap.entrySet()) {
      e.getValue().start();
    }
    return false;
  }

  @Override
  public boolean stop() {
    log.info("开始关闭文件监控插件");
    for (Map.Entry<String, WathchServiceTaskInfo> e : taskInfoMap.entrySet()) {
      e.getValue().stop();
    }
    return true;
  }

  public WatchServicePlugin addTask(String id,FileSyncWatcher task) {
    return addTask(id,task, true, true);
  }

  public WatchServicePlugin addTask(String id,FileSyncWatcher task, boolean daemon, boolean enable) {
    taskInfoMap.put(id,new WathchServiceTaskInfo(task, daemon, enable));
    return this;
  }
  public WathchServiceTaskInfo getTask(String id) {
    return taskInfoMap.get(id);
  }

  /**
   * 开启自动id的监控任务
   * @param id
   */
  public void start(String id) {
    WathchServiceTaskInfo wathchServiceTaskInfo = taskInfoMap.get(id);
    wathchServiceTaskInfo.start();
  }

  public Map<String, WathchServiceTaskInfo> getTaskInfo() {
    return taskInfoMap;
  }
}
