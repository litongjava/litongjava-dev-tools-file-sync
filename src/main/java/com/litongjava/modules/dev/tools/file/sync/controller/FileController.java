package com.litongjava.modules.dev.tools.file.sync.controller;

import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.Kv;
import com.litongjava.modules.dev.tools.file.sync.services.FileService;
import com.litongjava.utils.vo.JsonBean;

import lombok.extern.slf4j.Slf4j;

/**
 * @author create by ping-e-lee on 2021年7月11日 上午3:58:40 
 * @version 1.0 
 * @desc
 */
@Path("file")
@Slf4j
public class FileController extends Controller{
  FileService fileService = Aop.get(FileService.class);
  /**
   * 同步文件
   * @param kv
   */
  public void sync(Kv kv) {
    int status=fileService.sync(kv);
    log.info("同步状态:{}",status);
    JsonBean<Void> jsonBean = new JsonBean<>();
    jsonBean.setCode(status);
    renderJson(jsonBean);
  }
}
