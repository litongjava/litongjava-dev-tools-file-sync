package com.litongjava.modules.dev.tools.file.sync.utils;

import static org.junit.Assert.*;

import java.util.Vector;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.litongjava.utils.file.PathUtils;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午9:16:39 
 * @version 1.0 
 * @desc
 */
public class JschUtilsTest {

  JschChannel jschChannel = null;
  String dst = "/root/dev_workspace/java/java-study/java-ee-spring-boot-study/java-ee-spring-boot-2.1.6-study/java-ee-spring-boot-2.1.6-hello/src/main/java/com/litongjava/spring/boot/hello/TestController.java";

  private ChannelSftp connect() {
    ChannelSftp channel = null;
    try {
      jschChannel = new JschChannel();
      channel = jschChannel.connect("root", "00000000", "192.168.0.6", 22, 60000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return channel;
  }

  @Test
  public void testLs() {

    ChannelSftp channel = connect();

    String dstPath = FilenameUtils.getPath(dst);
    try {
      channel.ls(dstPath);
    } catch (SftpException e) {
      String message = e.getMessage();
      if ("No such file".equals(message)) { // 目录不存在,创建目录
        // mkdirs(sftpChannel,dstPath);
      }

    }

    jschChannel.closeChannel();
  }

  @Test
  public void testMkdirs() {
    ChannelSftp channel = connect();
    String dst = "/root/dev_workspace/java/java-study/java-ee-spring-boot-study/java-ee-spring-boot-2.1.6-study/java-ee-spring-boot-2.1.6-hello/src/main/java/com/litongjava/spring/boot/hello/TestController.java";
    String dstPath = PathUtils.getParentPath(dst,"/");
//    FilenameUtils.getPathNoEndSeparator(filename)
    System.out.println(dstPath);
    JschUtils.mkdirs(channel, dstPath);
  }
  
  /**
   * 测试上传文件夹
   * 测试结果,输出拒绝访问错误
   * 4: java.io.FileNotFoundException: E:\dev_workspace\java\java-study\java-ee-spring-boot-study\java-ee-spring-boot-2.3.0-study (拒绝访问。)
  at com.jcraft.jsch.ChannelSftp.put(ChannelSftp.java:487)
  at com.jcraft.jsch.ChannelSftp.put(ChannelSftp.java:368)
  at com.litongjava.modules.dev.tools.file.sync.utils.JschUtilsTest.testUploadFoler(JschUtilsTest.java:73)
   */
  @Test
  public void testUploadFoler() {
    String src="E:\\dev_workspace\\java\\java-study\\java-ee-spring-boot-study\\java-ee-spring-boot-2.3.0-study";
    String dst = "/root/dev_workspace/java/java-study/java-ee-spring-boot-study/ava-ee-spring-boot-2.3.0-study";
    ChannelSftp channel = connect();
    try {
      channel.put(src, dst, ChannelSftp.OVERWRITE);
    } catch (SftpException e) {
      e.printStackTrace();
    }finally {
      jschChannel.closeChannel();
    }
  }
  

}
