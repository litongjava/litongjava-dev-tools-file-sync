package com.litongjava.modules.dev.tools.file.sync.controller;

import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.litongjava.modules.dev.tools.file.sync.plugin.WatchServicePluginUtils;

/**
 * @author create by ping-e-lee on 2021年7月10日 下午6:47:45 
 * @version 1.0 
 * @desc
 */
@Path("plugin/watch-service")
public class WatchServicePluginController extends Controller{
  
  public void taskInfo() {
    renderJson(WatchServicePluginUtils.getPlugin().getTaskInfo());
  }
  }
