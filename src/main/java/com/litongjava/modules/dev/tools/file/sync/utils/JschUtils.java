package com.litongjava.modules.dev.tools.file.sync.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.litongjava.utils.file.PathUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午8:22:31 
 * @version 1.0 
 * @desc
 */
@Slf4j
public class JschUtils {

  /**
   * 上传文件到linux服务器
   * @param localFilePath
   * @param remoteFilePath
   * @param remoteIp
   * @param remotePort
   * @param remoteUser
   * @param remotePswd
   */
  public static void upload(String src, String dst, String remoteHost, String remotePort, String remoteUser, String remotePswd) {
    JschChannel jschChannel = new JschChannel();
    ChannelSftp channel = null;
    try {
      channel = jschChannel.connect(remoteUser, remotePswd, remoteHost, Integer.valueOf(remotePort), 60000);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    try {
      log.info("upload {} to {}", FilenameUtils.getName(src), PathUtils.getParentPath(dst, "/"));
      /**
       * 测试发现如果远程目录不存在会上传失败
       * 判断远程目录是否存在,如果不存在,创建目录
       */
      mkdirs(channel, PathUtils.getParentPath(dst, "/"));
      channel.put(src, dst, ChannelSftp.OVERWRITE);
    } catch (SftpException e) {
      e.printStackTrace();
    } finally {
      jschChannel.closeChannel();
    }
  }

  public static void delete(String remoteFilePath, String remoteHost, String remotePort, String remoteUser, String remotePswd) {
    JschChannel jschChannel = null;
    ChannelSftp channel = null;
    try {
      jschChannel = new JschChannel();
      channel = jschChannel.connect(remoteUser, remotePswd, remoteHost, Integer.valueOf(remotePort), 60000);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    try {
      log.info("delete {}", remoteFilePath);
      channel.rm(remoteFilePath);
    } catch (SftpException e) {
      e.printStackTrace();
    } finally {
      jschChannel.closeChannel();
    }
  }

  /**
   * 递归创建文件夹
   * @param sftpChannel
   * @param dst
   */
  public static void mkdirs(ChannelSftp channel, String dstPath) {
    // 判断本目录是否存在,如果不存在,创建父目录
    List<String> notExistsPath = new ArrayList<>();
    getNotExistsPath(channel, dstPath, notExistsPath);
    for (int i = notExistsPath.size() - 1; i > -1; i--) {
      String path = notExistsPath.get(i);
      try {
        log.info("mkdir:{}", path);
        channel.mkdir(path);
      } catch (SftpException e) {
        log.error("mkdir {} error:{}", path, e.getMessage());
      }
    }

  }

  private static void getNotExistsPath(ChannelSftp channel, String dstPath, List<String> notExistsPath) {
    try {
      channel.ls(dstPath);
    } catch (SftpException e) {
      String message = e.getMessage();
      log.info("exception:{},dstPath:{}", message, dstPath);
      notExistsPath.add(dstPath);
      getNotExistsPath(channel, PathUtils.getParentPath(dstPath, "/"), notExistsPath);
    }
  }

  /**
   * 同步文件夹
   * @param srcPath
   * @param dstPath
   * @param host
   * @param port
   * @param user
   * @param pswd
   * @return
   * 0 成功
   * 1 失败
   */
  public static int uploadFolder(String localPath, String remotePath, String host, String port, String user, String pswd) {
    JschChannel jschChannel = new JschChannel();
    ChannelSftp channel = null;
    try {
      channel = jschChannel.connect(user, pswd, host, Integer.valueOf(port), 60000);
    } catch (Exception e) {
      e.printStackTrace();
      return 1;
    }
    /**
     * 删除远程目录
     * 测试结果,当目录存在时出现异常4: Failure
     */
//    try {
//      channel.rmdir(remotePath);
//      log.info("删除远程目录成功");
//    } catch (SftpException e) {
//      e.printStackTrace();
//      log.info("删除远程目录出现异常:{}",e.getMessage());
//    }
    log.info("upload {} to {}", FilenameUtils.getName(localPath), PathUtils.getParentPath(remotePath, "/"));
    Path path = Paths.get(localPath);
    
    /**
     * 遍历目录,上传文件
     * 访问时顺序访问
     */
    SimpleFileVisitor<Path> simpleFileVisitor = new JschUploadFolerFileVisitor(channel,localPath,remotePath);
 
    /**
     * 遍历文件并上传
     */
    try {
      Files.walkFileTree(path, simpleFileVisitor);
    } catch (Exception e) {
      e.printStackTrace();
      return 1;
    } finally {
      jschChannel.closeChannel();
      
    }
    return 0;
  }

  /**
   * 
   * @param absolutePath
   * @param string 
   * @return
   */
  public static String getRemoteFullPath(String src, String localPath, String remotePath) {

    // 如果匹配成功返回0
    int lastIndexOf = src.lastIndexOf(localPath);

    if (lastIndexOf == 0) {
      String substring = src.substring(localPath.length());
      // log.info("substring : " + substring);
      // windows上的文件分割符替换为linux上的文件分割服务
      String replace = substring.replace(File.separator, "/");
      String remoteFilePath = remotePath + replace;
      return remoteFilePath;
    } else {
      return null;
      // log.info("没有匹配到路径:{}", localFilePath);
    }
  }
}
