package com.litongjava.modules.dev.tools.file.sync.utils;

/**
 * @author create by ping-e-lee on 2021年7月11日 上午10:53:53 
 * @version 1.0 
 * @desc
 */

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.FilenameUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.litongjava.utils.file.PathUtils;
import com.litongjava.utils.log.LogUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * create by ping-e-lee on 2021年7月11日 上午10:59:17
 * @desc 
 */
@Slf4j
public class JschUploadFolerFileVisitor extends SimpleFileVisitor<Path> {
  private ChannelSftp channel = null;
  private String localPath = null;
  private String remotePath = null;

  public JschUploadFolerFileVisitor(ChannelSftp channel, String localPath, String remotePath) {
    this.channel = channel;
    this.localPath = localPath;
    this.remotePath = remotePath;
  }

  /**
   * 子目录不做处理,因为访问子目录出现拒绝访问异常
   */
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  // 在访问文件时触发该方法
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String src = file.toString();
    String getRemoteFullPath = JschUtils.getRemoteFullPath(src, localPath, remotePath);
    try {
      log.info("upload {} to {}", FilenameUtils.getName(src), PathUtils.getParentPath(getRemoteFullPath, "/"));
      /**
       * 测试发现如果远程目录不存在会上传失败
       * 判断远程目录是否存在,如果不存在,创建目录
       */
      JschUtils.mkdirs(channel, PathUtils.getParentPath(getRemoteFullPath, "/"));
      channel.put(src, getRemoteFullPath, ChannelSftp.OVERWRITE);
    } catch (SftpException e) {
      log.error(LogUtils.getStackTraceInfo(e));
    }
    return FileVisitResult.CONTINUE;
  }

  // 在访问失败时触发该方法
  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    // 写一些具体的业务逻辑
    return super.visitFileFailed(file, exc);
  }

  // 在访问目录之后触发该方法
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    // 写一些具体的业务逻辑
    return super.postVisitDirectory(dir, exc);
  }

}
