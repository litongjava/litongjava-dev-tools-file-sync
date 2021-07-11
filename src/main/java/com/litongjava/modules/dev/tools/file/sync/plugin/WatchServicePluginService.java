package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.util.List;

import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月10日 上午12:05:05 
 * @version 1.0 
 * @desc
 */
@Slf4j
public class WatchServicePluginService {

  public void restart() {
    // 1.从数据库中查询出定时任务信息
    String sql = "SELECT id,local_path,remote_path,remote_ip,remote_port,remote_user,remote_pswd,name FROM sync_info WHERE STATUS=1 AND is_del=0";
    List<SyncInfo> find = SyncInfo.dao.find(sql);
    // 停止旧的文件监控
    WatchServicePlugin oldPlugin = WatchServicePluginUtils.getPlugin();
    if (oldPlugin != null) {
      log.info("停止旧的文件监控");
      oldPlugin.stop();
    }
    // 创建新的插件
    WatchServicePlugin newPlugin = new WatchServicePlugin();
    // 保存新插件
    WatchServicePluginUtils.setPlugin(newPlugin);
    // 添加监控
    for (SyncInfo e : find) {
      // 添加监控
      log.info("id:{},监控目录:{}", e.getId(), e.getLocalPath());
      FileWatcher fileWatcher = new FileWatcher(e);
      newPlugin.addTask(e.getId(),fileWatcher);
    }
    // 开启新的文件检测服务
    log.info("开启文件检测服务");
    newPlugin.start();
  }
}
