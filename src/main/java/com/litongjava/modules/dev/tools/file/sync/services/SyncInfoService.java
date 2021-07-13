package com.litongjava.modules.dev.tools.file.sync.services;

import java.util.List;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.litongjava.modules.dev.tools.file.sync.dataobj.SyncInfoStatus;
import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;
import com.litongjava.modules.dev.tools.file.sync.plugin.FileSyncWatcher;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePlugin;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePluginUtils;
import com.litongjava.modules.dev.tools.file.sync.plugin.WathchServiceTaskInfo;
import com.litongjava.utils.vo.JsonBean;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月9日 下午11:58:32 
 * @version 1.0 
 * @desc
 */
@Slf4j
public class SyncInfoService {

  /**
   * 保存和修改时更新文件监控
   * @param tableName
   * @param kv
   * @return
   */
  
  public JsonBean<Void> saveOrUpdateAfter(String tableName, Kv kv) {

    //kv转为SyncInfo
    SyncInfo syncInfo = toRecord(kv);
    
    WatchServicePlugin plugin = WatchServicePluginUtils.getPlugin();
    
    /**
     * 关闭监控,开启新的监控
     */
    String primarykeyName="id";
    if (kv.containsKey(primarykeyName) && !StrKit.isBlank(kv.getStr(primarykeyName))) {// 更新
      log.info("数据更新:{}",syncInfo.getId());
      WathchServiceTaskInfo task = plugin.getTask(syncInfo.getId());
      if(task!=null) {
        task.stop();
      }
      
      /**
       * 如果是开启状态开启新的监控 
       */
      if(SyncInfoStatus.enable.equals(syncInfo.getStatus())) {
        String sql = "SELECT id,local_path,remote_path,remote_ip,remote_port,remote_user,remote_pswd,name FROM sync_info WHERE id=? and STATUS=1 AND is_del=0";
        syncInfo= SyncInfo.dao.findFirst(sql, syncInfo.getId());
        plugin.addTask(syncInfo.getId(), new FileSyncWatcher(syncInfo));
        plugin.start(syncInfo.getId());
      }
    }
    

      
    return null;
  }

  @SuppressWarnings("unchecked")
  private SyncInfo toRecord(Kv kv) {
    Record record = new Record();
    record.setColumns(kv);
    SyncInfo syncInfo = new SyncInfo();
    syncInfo.put(record);
    return syncInfo;
  }

}
