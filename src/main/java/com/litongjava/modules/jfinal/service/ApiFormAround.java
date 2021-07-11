package com.litongjava.modules.jfinal.service;



import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;
import com.litongjava.modules.dev.tools.file.sync.services.SyncInfoService;
import com.litongjava.modules.jfinal.vo.PageJsonBean;
import com.litongjava.utils.vo.JsonBean;

/**
 * @author litong
 * @date 2020年9月21日_下午7:37:25 
 * @version 1.0 
 * @desc
 */
public class ApiFormAround {
  private SyncInfoService syncInfoService=Aop.get(SyncInfoService.class);

  public void listBefore(int pageNo, int pageSize, String tableName, String columns, String orderBy, Boolean isAsc, Kv kv) {
  }

  public void listAfter(int pageNo, int pageSize, String tableName, String columns, String orderBy, Boolean isAsc, Kv kv,
      PageJsonBean<Record> pageJsonBean) {
  }

  public void getByIdBefore(String tableName, Kv kv) {
  }

  public void getByIdAfter(String tableName, Kv kv, JsonBean<Record> jsonBean) {
  }

  public void removeByIdBefore(String tableName, Kv kv) {
  }

  public void removeByIdAfter(String tableName, Kv kv, JsonBean<Void> jsonBean) {
    if("sync_info".equals(tableName)) {
      syncInfoService.saveOrUpdateAfter(tableName,kv);
    }
  }

  public void removeByIdsBefore(String tableName, Kv kv) {
  }

  public JsonBean<Void> removeByIdsAfter(String tableName, Kv kv, JsonBean<Void> jsonBean) {
    if("sync_info".equals(tableName)) {
      return syncInfoService.saveOrUpdateAfter(tableName,kv);
    }
    return null;
  }

  public JsonBean<Void> saveOrUpdateBefore(String tableName, Kv kv) {
    return null;
   
  }

  public JsonBean<Void> saveOrUpdateAfter(String tableName, Kv kv) {
    if("sync_info".equals(tableName)) {
      return syncInfoService.saveOrUpdateAfter(tableName,kv);
    }
    return null;
  }

  public JsonBean<Void> updateByIdBefore(String tableName, Kv kv) {
    return null;
  }

  public void updateByIdAfter(String tableName, Kv kv, JsonBean<Void> jsonBean) {
  }
}
