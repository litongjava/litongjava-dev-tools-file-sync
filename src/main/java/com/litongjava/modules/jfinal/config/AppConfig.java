package com.litongjava.modules.jfinal.config;

import com.jfinal.aop.Aop;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.paragetter.ParaProcessorBuilder;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePlugin;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePluginService;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePluginUtils;
import com.litongjava.modules.jfinal.getter.KvGetter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppConfig extends JFinalConfig {

  public void configConstant(Constants me) {
    me.setInjectDependency(true);
    me.setInjectSuperClass(true);
    ParaProcessorBuilder.me.regist(Kv.class, KvGetter.class, null);

  }

  public void configRoute(Routes me) {
    me.setMappingSuperClass(true);
    me.scan("com.litongjava.modules.dev.tools.file.sync.controller.");
    me.scan("com.litongjava.modules.jfinal.controller.");
  }

  @Override
  public void configEngine(Engine me) {
  }

  @Override
  public void configPlugin(Plugins me) {
    log.info("开始配置插件");
    DbConfig.config(me);
  }

  @Override
  public void configInterceptor(Interceptors me) {
  }

  @Override
  public void configHandler(Handlers me) {
    
  }
  @Override
  public void onStart() {
    //启动文件检测服务
    startSyncInfoWatchService();
  }
  private void startSyncInfoWatchService() {
    WatchServicePluginService syncInfoWatchService = Aop.get(WatchServicePluginService.class);
    syncInfoWatchService.restart();
  }
  @Override
  public void onStop() {
    WatchServicePlugin oldPlugin = WatchServicePluginUtils.getPlugin();
//    log.info("oldPlugin : " + oldPlugin);
    oldPlugin.stop();
  }

}