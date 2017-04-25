package org.base.component.message.SMS;


/**
 * 短信基础配置信息
 * @author yong
 *
 */
public class SMSBaseConfigInfo {
	//ESB接口地址
	private String soapUrl;
	// 路由编号，由EAI分配提供，短信接口固定为EAI_SmsInformService
	private String messageType;
	// 用户名，由EAI分配提供
	private String userId;
	// 用户密码，由EAI分配提供
	private String userPwd;
	// 用户私钥文件路径，由EAI分配提供
	private String privateKeyPath;
	
	public String getSoapUrl() {
		return soapUrl;
	}
	
	public void setSoapUrl(String soapUrl) {
		this.soapUrl = soapUrl;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserPwd() {
		return userPwd;
	}
	
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	public String getPrivateKeyPath() {
		return privateKeyPath;
	}
	
	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}
}
