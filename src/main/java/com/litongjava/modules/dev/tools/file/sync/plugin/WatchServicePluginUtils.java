package com.litongjava.modules.dev.tools.file.sync.plugin;

/**
 * @author create by ping-e-lee on 2021年7月10日 上午12:18:33 
 * @version 1.0 
 * @desc
 */
public class WatchServicePluginUtils {
  
  private static WatchServicePlugin watchServicePlugin=null;
  
  public static void setPlugin(WatchServicePlugin plugin ) {
    WatchServicePluginUtils.watchServicePlugin=plugin;
  }
  public static WatchServicePlugin getPlugin() {
    return watchServicePlugin;
  }
}
