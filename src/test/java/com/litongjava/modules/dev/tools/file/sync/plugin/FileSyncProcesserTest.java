package com.litongjava.modules.dev.tools.file.sync.plugin;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

/**
 * @author create by ping-e-lee on 2021年7月13日 上午11:47:01 
 * @version 1.0 
 * @desc
 */
public class FileSyncProcesserTest {

  @Test
  public void test() {
    String path="E:\\dev_workspace\\java\\java-study\\java-ee-spring-cloud-study\\shop_parent\\shop_service_test\\target\\classes\\META-INF\\maven\\com.litongjava\\shop_service_test";
    File file = new File(path);
    System.out.println(file.exists());
    System.out.println(file.isDirectory());
    
    String extension = FilenameUtils.getExtension(path);
    System.out.println(extension);
    
  }

}
