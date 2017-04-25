package org.base.component.email;

import java.util.Properties;

public class Email
{
  public final static String TRIGGER_MAILS="triggeremails";
  public final static String REQUEST_METHOD="SendMail";
  public final static String TRIGGER_ID="1";
  private String mailServerHost;
  private String mailServerPort = "25";
  private String fromAddress;
  private String toAddress;
  private String userName;
  private String password;
  private boolean validate = false;
  private String subject;
  private String content;
  private String[] attachFileNames;
  private String affix = "";
  private String emailSender;
  private String httpUrl;
  private String usertoken;
  private String emailType;

  private String affixName = "";

  public Properties getProperties()
  {
    Properties p = new Properties();
    p.put("mail.smtp.host", this.mailServerHost);
    p.put("mail.smtp.port", this.mailServerPort);
    p.put("mail.smtp.auth", this.validate ? "true" : "false");
    return p;
  }

  public String getMailServerHost() {
    return this.mailServerHost;
  }

  public void setMailServerHost(String mailServerHost) {
    this.mailServerHost = mailServerHost;
  }

  public String getMailServerPort() {
    return this.mailServerPort;
  }

  public void setMailServerPort(String mailServerPort) {
    this.mailServerPort = mailServerPort;
  }

  public String getFromAddress() {
    return this.fromAddress;
  }

  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  public String getToAddress() {
    return this.toAddress;
  }

  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isValidate() {
    return this.validate;
  }

  public void setValidate(boolean validate) {
    this.validate = validate;
  }

  public String getSubject() {
    return this.subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String[] getAttachFileNames() {
    return this.attachFileNames;
  }

  public void setAttachFileNames(String[] attachFileNames) {
    this.attachFileNames = attachFileNames;
  }

  public String getAffix() {
    return this.affix;
  }

  public void setAffix(String affix) {
    this.affix = affix;
  }

  public String getAffixName() {
    return this.affixName;
  }

  public void setAffixName(String affixName) {
    this.affixName = affixName;
  }

  public String getEmailSender() {
		return emailSender;
  }
	
  public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
  }

	public String getHttpUrl() {
		return httpUrl;
	}
	
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getUsertoken() {
		return usertoken;
	}

	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	
  
}