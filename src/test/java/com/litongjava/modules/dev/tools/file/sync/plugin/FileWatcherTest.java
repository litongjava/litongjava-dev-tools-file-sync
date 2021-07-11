package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.io.File;

import org.junit.Test;

import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午8:24:43 
 * @version 1.0 
 * @desc
 */
public class FileWatcherTest {

  @Test
  public void testUpload() {
    File file = new File("E:\\dev_workspace\\java\\java-study\\java-ee-spring-boot-study\\java-ee-spring-boot-2.1.6-study\\java-ee-spring-boot-2.1.6-hello\\src\\main\\java\\com\\litongjava\\spring\\boot\\hello\\TestController.java");
    SyncInfo syncInfo = new SyncInfo();
    syncInfo.setLocalPath("E:\\dev_workspace\\java\\java-study\\java-ee-spring-boot-study\\java-ee-spring-boot-2.1.6-study");
    syncInfo.setRemotePath("/root/dev_workspace/java/java-study/java-ee-spring-boot-study/java-ee-spring-boot-2.1.6-study");
    syncInfo.setRemoteIp("192.168.0.6");
    syncInfo.setRemotePort("22");
    syncInfo.setRemoteUser("root");
    syncInfo.setRemotePswd("00000000");
    
    FileWatcher fileWatcher = new FileWatcher(syncInfo);
    fileWatcher.upload(file, syncInfo);
    
  }
  @Test
  public void testFilePathSeparator() {
    System.out.println(File.pathSeparator);//;
    System.out.println(File.separator);//\
  }

}
