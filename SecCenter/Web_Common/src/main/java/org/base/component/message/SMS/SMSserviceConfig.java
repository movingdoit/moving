package org.base.component.message.SMS;

/**
 * 短信服务配置
 * @author yong
 *
 */
public class SMSserviceConfig {
	
	private String appName;
	
	private String signString;
	
	private String subServiceCode;
	
	private String userName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSignString() {
		return signString;
	}

	public void setSignString(String signString) {
		this.signString = signString;
	}

	public String getSubServiceCode() {
		return subServiceCode;
	}

	public void setSubServiceCode(String subServiceCode) {
		this.subServiceCode = subServiceCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
