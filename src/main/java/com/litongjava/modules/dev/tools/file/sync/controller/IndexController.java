package com.litongjava.modules.dev.tools.file.sync.controller;

import com.jfinal.core.Controller;
import com.jfinal.core.Path;

/**
 * @author create by ping-e-lee on 2021年7月9日 下午4:42:49 
 * @version 1.0 
 * @desc
 */
@Path("/")
public class IndexController extends Controller {
  public void index() {
//    renderText("litongjava-dev-tools-file-sync");
//    redirect("/index.html");
    redirect("/backend/sync_info/sync_info_list.html");
  }

  public void version() {
    renderText("v1.0-2021年7月9日16:44:11");
  }
}
