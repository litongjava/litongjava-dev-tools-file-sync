package com.litongjava.modules.dev.tools.file.sync.services;

import com.jfinal.kit.Kv;
import com.litongjava.modules.dev.tools.file.sync.model.SyncInfo;
import com.litongjava.modules.dev.tools.file.sync.utils.JschUtils;

/**
 * @author create by ping-e-lee on 2021年7月11日 上午4:00:16 
 * @version 1.0 
 * @desc
 */
//@Slf4j
public class FileService {

  public int sync(Kv kv) {
    SyncInfo syncInfo = SyncInfo.dao.findById(kv.get("id"));
    return JschUtils.uploadFolder(syncInfo.getLocalPath(), syncInfo.getRemotePath(), syncInfo.getRemoteIp(), syncInfo.getRemotePort(), 
        syncInfo.getRemoteUser(), syncInfo.getRemotePswd());
  }

}
