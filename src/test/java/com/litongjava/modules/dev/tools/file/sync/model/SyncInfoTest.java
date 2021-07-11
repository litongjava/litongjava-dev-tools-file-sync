package com.litongjava.modules.dev.tools.file.sync.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午12:31:05 
 * @version 1.0 
 * @desc
 */
public class SyncInfoTest {

  @Test
  public void test() {
    /**
     * 创建一个kv
     */
    Kv kv = Kv.by("local_path", "1234");
    kv.put("remote_path", "34434");
    /**
     * kv转为record,record转为实体类
     */
    Record record = new Record();
    record.setColumns(kv);
    SyncInfo syncInfo = new SyncInfo();
    syncInfo.put(record);
    System.out.println(syncInfo);
  }

}
