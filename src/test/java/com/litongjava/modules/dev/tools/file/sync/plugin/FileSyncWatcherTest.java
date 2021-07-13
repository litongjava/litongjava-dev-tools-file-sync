package com.litongjava.modules.dev.tools.file.sync.plugin;

import java.io.File;

import org.junit.Test;

/**
 * @author create by ping-e-lee on 2021年7月12日 下午9:55:57 
 * @version 1.0 
 * @desc
 */
public class FileSyncWatcherTest {

  @Test
  public void testIsDirecotry() {
    String path="E:\\dev_workspace\\java\\java-study\\java-ee-spring-cloud-study\\shop_parent\\shop_service_test\\target\\classes\\META-INF\\maven\\com.litongjava\\shop_service_test";
    File file = new File(path);
    System.out.println(file.isDirectory());
  }

}
