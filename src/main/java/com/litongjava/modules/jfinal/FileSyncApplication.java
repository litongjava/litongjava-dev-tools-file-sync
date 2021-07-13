package com.litongjava.modules.jfinal;

import com.jfinal.server.undertow.UndertowConfig;
import com.jfinal.server.undertow.UndertowServer;
import com.litongjava.modules.jfinal.config.AppConfig;
import com.litongjava.modules.jfinal.utils.PropKitUtils;
import com.litongjava.utils.ip.IpUtils;

/**
 * @author create by ping-e-lee on 2021年7月13日 上午12:37:33 
 * @version 1.0 
 * @desc
 */

public class FileSyncApplication {
  private static String configFileName = PropKitUtils.configFileName;
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    UndertowServer server = UndertowServer.create(AppConfig.class, configFileName);
    server.start();
    long end = System.currentTimeMillis();
    System.out.println("启动完成,共使用了" + (end - start) + "ms");
    UndertowConfig config = server.getUndertowConfig();
    IpUtils.getThisUrl(config.getPort(), config.getContextPath());
  
  }
}
