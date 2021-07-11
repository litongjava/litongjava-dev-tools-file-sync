package com.litongjava.modules.dev.tools.file.sync.utils;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JschChannel {
  Session session = null;
  Channel channel = null;

  public void closeChannel() {
    if (channel != null) {
      channel.disconnect();
    }
    if (session != null) {
      session.disconnect();
    }
  }

  public ChannelSftp connect(String remoteUser, String remotePswd, String remoteHost, Integer remotePort,int timeout) throws JSchException {
    // 创建JSch对象
    JSch jsch = new JSch();
    // 根据用户名，主机ip，端口获取一个Session对象
    session = jsch.getSession(remoteUser, remoteHost, remotePort);
    log.debug("Session created.");
    if (remotePswd != null) {
      // 设置密码
      session.setPassword(remotePswd);
    }
    Properties config = new Properties();
    config.put("StrictHostKeyChecking", "no");
    // 为Session对象设置properties
    session.setConfig(config);
    // 设置timeout时间
    session.setTimeout(timeout);
    // 通过Session建立链接
    session.connect();
    log.debug("Session connected.");

    log.debug("Opening Channel.");
    // 打开SFTP通道
    channel = session.openChannel("sftp");
    // 建立SFTP通道的连接
    channel.connect();
    log.debug("Connected successfully to {} ,as ftpUserName :{} returning:{} ", remoteHost + ":" + remotePort, remoteUser, channel);
    return (ChannelSftp)channel;
  }
}