package com.litongjava.modules.dev.tools.file.sync.plugin;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午5:46:12 
 * @version 1.0 
 * @desc
 */
public class WathchServiceTaskInfo {

  boolean daemon;
  boolean enable;
  FileSyncWatcher task;

  public WathchServiceTaskInfo(FileSyncWatcher task, boolean daemon, boolean enable) {
    this.task = task;
    this.daemon = daemon;
    this.enable = enable;
  }

  public void stop() {
    task.exit();
    task.close();
  }

  public void start() {
    task.start();
  }

}
