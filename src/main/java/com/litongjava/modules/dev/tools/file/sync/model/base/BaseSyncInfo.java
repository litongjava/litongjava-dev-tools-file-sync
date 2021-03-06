package com.litongjava.modules.dev.tools.file.sync.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSyncInfo<M extends BaseSyncInfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}
	
	public void setLocalPath(java.lang.String localPath) {
		set("local_path", localPath);
	}
	
	public java.lang.String getLocalPath() {
		return getStr("local_path");
	}
	
	public void setRemotePath(java.lang.String remotePath) {
		set("remote_path", remotePath);
	}
	
	public java.lang.String getRemotePath() {
		return getStr("remote_path");
	}
	
	public void setRemoteIp(java.lang.String remoteIp) {
		set("remote_ip", remoteIp);
	}
	
	public java.lang.String getRemoteIp() {
		return getStr("remote_ip");
	}
	
	public void setRemoteUser(java.lang.String remoteUser) {
		set("remote_user", remoteUser);
	}
	
	public java.lang.String getRemoteUser() {
		return getStr("remote_user");
	}
	
	public void setRemotePswd(java.lang.String remotePswd) {
		set("remote_pswd", remotePswd);
	}
	
	public java.lang.String getRemotePswd() {
		return getStr("remote_pswd");
	}
	
	public void setRemotePort(java.lang.String remotePort) {
		set("remote_port", remotePort);
	}
	
	public java.lang.String getRemotePort() {
		return getStr("remote_port");
	}
	
	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}
	
	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	public java.lang.String getStatus() {
		return getStr("status");
	}
	
	public void setIsDel(java.lang.String isDel) {
		set("is_del", isDel);
	}
	
	public java.lang.String getIsDel() {
		return getStr("is_del");
	}
	
}

