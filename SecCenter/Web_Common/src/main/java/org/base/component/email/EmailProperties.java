package org.base.component.email;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.base.component.utils.XmlObject;
import org.dom4j.Document;
import org.dom4j.Element;

//import com.dmdelivery.webservice.DMdeliveryLoginType;
//import com.dmdelivery.webservice.DMdeliverySoapAPIPort;
//import com.dmdelivery.webservice.DMdeliverySoapAPIPortProxy;
//import com.dmdelivery.webservice.RecipientNameValuePairType;
//import com.dmdelivery.webservice.RecordResultType;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class EmailProperties {

	private static Email mailInfo = new Email();
	private static Properties prop = new Properties();
	
	private static final Log log = LogFactory.getLog(EmailProperties.class);

	static {
		InputStream in = null;
		try {
			String filePath = EmailProperties.class.getResource("/").getPath()+"email.properties";
//			filePath = filePath.substring(0,
//					filePath.length() - "classes/".length())
//					+ "conf/email.properties";
			in = new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据key获取properties的value
	 *
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return prop.getProperty(key).trim();
	}

	/**
	 * 封装邮件发送对象
	 *
	 * @param subject
	 * @param content
	 * @param toAddress
	 */
	private static void getMailInfo(String subject, String content,
			String toAddress,String emailType) {
		mailInfo.setMailServerHost(getValue("mailServerHost"));
		mailInfo.setMailServerPort(getValue("mailServerPort"));
		mailInfo.setValidate(true);
		mailInfo.setUserName(getValue("userName"));
		mailInfo.setPassword(getValue("password"));// 您的邮箱密码
		mailInfo.setFromAddress(getValue("fromAddress"));
		mailInfo.setEmailSender(getValue("emailSender"));
		mailInfo.setHttpUrl(getValue("httpUrl"));
		mailInfo.setUsertoken(getValue("usertoken"));
		mailInfo.setToAddress(toAddress.trim());
		mailInfo.setEmailType(emailType);
		if (subject != null && !"".equals(subject)) {
			mailInfo.setSubject(subject);
		} else {
			mailInfo.setSubject(getValue("subject"));
		}
		if (content != null && !"".equals(content)) {
			mailInfo.setContent(content);
		} else {
			mailInfo.setContent(getValue("content"));
		}
		
	}
	
	/**
	 * WEBPOWER邮件发送
	 * 通过webservice服务发送邮件
	 * 发送邮件必须有活动ID，邮件ID，组ID，
	 * 需要先将数据通过webservice上传到服务器
	 * @param dataMap 邮件发送数据
	 * @return boolean
	 */
	public static boolean sendWPEmail(HashMap<String,String> dataMap, String type) {
		if(dataMap.size()<=0) {
			return false;
		}
		// 封装邮件数据，邮件发送之前需要先将数据封装并上传到服务器
//		RecipientNameValuePairType[] data = new RecipientNameValuePairType[dataMap.size()];
		Iterator iterator = dataMap.keySet().iterator();
		int i=0;
		while(iterator.hasNext()) {
			String key = iterator.next().toString();
//			data[i] = new RecipientNameValuePairType(key, dataMap.get(key));
			i++;
		}
		
		// 获取活动ID，必需字段
		int campaign = Integer.parseInt(getValue("campaignID"));
		
		// 获取组ID，必需字段，可有多个组ID
		String[] group = getValue("group").split(",");
		if(group.length<=0) {
			return false;
		}
		int[] groups = new int[group.length];
		for(i=0;i<group.length;i++) {
			groups[i] = Integer.parseInt(group[i]);
		}
		
		// 邮件ID，必需字段，需要在WEBPOWER平台上预先设定邮件
		int mailingID = 0;
		if(type.equals("2")) {
			mailingID = Integer.parseInt(getValue("FindPasswordEmailID")); // 找回密码邮件
		}else if(type.equals("3")) {
			mailingID = Integer.parseInt(getValue("RegisterEmailID")); // 注册验证邮件
		}else {
			mailingID = Integer.parseInt(getValue("mailingID")); // 其他邮件
		}
		
		// 获取登录对象，必需字段，用于登录WEBPOWER邮件服务器
//		DMdeliveryLoginType login = new DMdeliveryLoginType(getValue("WPUsername"), getValue("WPPassword"));
		
//		DMdeliverySoapAPIPort client = new DMdeliverySoapAPIPortProxy();
		try {
//			RecordResultType result = client.addRecipient(login, campaign, groups, data, true, true); // 上传邮件数据，返回上传结果
//			
//			if(result.getStatus().getValue()!="ERROR") {
//				return client.sendSingleMailing(login, campaign, mailingID, result.getId()); // 如果上传成功，则发送邮件
//			}else {
//				log.error("Add Recipient Error: "+result.getStatusMsg()+"");
//			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return false;
		
	}

	/**
	 * 以文本文件的形式发送邮件
	 *
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param toAddress
	 *            目标地址
	 * @return
	 */
	public static boolean sendContentEmail(String subject, String content,
			String toAddress) throws Exception{
		if (toAddress != null && !"".equals(toAddress)) {
			getMailInfo(subject, content, toAddress,"");
			SimpleMailSender sms = new SimpleMailSender();
			return sms.sendTextMail(mailInfo);
		}
		return false;
	}

	/**
	 * 以html格式发送邮件信息
	 *
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param toAddress
	 *            目标地址
	 * @return
	 */
	public static boolean sendHtmlEmail(String subject, String content,
			String toAddress)throws Exception {
		if (toAddress != null && !"".equals(toAddress)) {
			getMailInfo(subject, content, toAddress,"");
			 return SimpleMailSender.sendHtmlMail(mailInfo);
		}
		return false;
	}
	
	/**
	 * 发送
	 * @param xmlObject
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpPostXML(String subject, String content,
			String toAddress,String emailType)throws Exception{
		getMailInfo(subject, content, toAddress,emailType);
		XmlObject xmlObject=objectToxml();
		SimpleMailSender sms = new SimpleMailSender();
		Document doc= sms.sendHttpPostXML(mailInfo.getHttpUrl(), xmlObject.toCompactXml());
		Element root=doc.getRootElement();
		String status=root.elementText("status");
		if(status!=null && "SUCCESS".equals(status)){
		    return toJson(status,root.elementText("data"));
		}else{
			return toJson(status,root.elementText("errormessage"));
		}
	}
	
	/**
	 * 发送Html:以后再开发
	 * @param xmlObject
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpPostHtmlXML(String subject, String content,
			String toAddress,String emailType)throws Exception{
		getMailInfo(subject, content, toAddress,emailType);
		XmlObject xmlObject=objectToxml();
		SimpleMailSender sms = new SimpleMailSender();
		Document doc= sms.sendHttpPostXML(mailInfo.getHttpUrl(), xmlObject.toCompactXml());
		Element root=doc.getRootElement();
		String status=root.elementText("status");
		if(status!=null && "SUCCESS".equals(status)){
		    return toJson(status,root.elementText("data"));
		}else{
			return toJson(status,root.elementText("errormessage"));
		}
	}
	
	public static String toJson(String status,String data){
		StringBuffer str=new StringBuffer();
		str.append("{\'status\':").append("\'"+status+"\',");
		str.append("\'data\':").append("\'"+data+"\'}");
		return str.toString();
	}
	
	public static XmlObject objectToxml()throws Exception{
		XmlObject xmlObject=new XmlObject("xmlrequest"); 
		XmlObject xusername=new XmlObject("username");
		xusername.setValue(mailInfo.getUserName());
		XmlObject xusertoken=new XmlObject("usertoken");
		xusertoken.setValue(mailInfo.getUsertoken());
		XmlObject xrequesttype=new XmlObject("requesttype");
		xrequesttype.setValue(Email.TRIGGER_MAILS);
		XmlObject xrequestmethod=new XmlObject("requestmethod");
		xrequestmethod.setValue(Email.REQUEST_METHOD);
		XmlObject xdetails=new XmlObject("details");
		XmlObject xemailaddress=new XmlObject("emailaddress");
		xemailaddress.setValue(mailInfo.getToAddress());
		XmlObject xsubject=new XmlObject("subject");
		xsubject.setValue(mailInfo.getSubject());
		XmlObject xtextbody=new XmlObject("htmlbody");
		xtextbody.setValue(mailInfo.getContent()==null?null:Base64.encode(mailInfo.getContent().getBytes()));
		XmlObject xtype=new XmlObject("type");
		xtype.setValue(mailInfo.getEmailType());
		XmlObject xtriggerid=new XmlObject("triggerid");
		xtriggerid.setValue(Email.TRIGGER_ID);
		
		xmlObject.addSubXmlObject(xusername);
		xmlObject.addSubXmlObject(xusertoken);
		xmlObject.addSubXmlObject(xrequesttype);
		xmlObject.addSubXmlObject(xrequestmethod);
		xmlObject.addSubXmlObject(xdetails);
		
		xdetails.addSubXmlObject(xemailaddress);
		xdetails.addSubXmlObject(xsubject);
		xdetails.addSubXmlObject(xtextbody);
		xdetails.addSubXmlObject(xtype);
		xdetails.addSubXmlObject(xtriggerid);
		return xmlObject;
	}
	
	
	
	public static String urltoHtml(String url) {
		StringBuffer email = new StringBuffer(
				"<html xmlns='http://www.w3.org/1999/xhtml'>");
		email.append("<head>");
		email.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		email.append("<title>验证地址</title>");
		email.append("</head><body>");
		email.append("<p><u><a href=\""+url+"\">"+url+"</a></u>   &#13;</p>");
		email.append("</body></html>");
		return email.toString();
	}
	
	public static String sendEmail(String url,String loginName){
		StringBuffer email = new StringBuffer(
		"<html xmlns='http://www.w3.org/1999/xhtml'>");
		email.append("<head>");
		email.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		email.append("<title>邮箱验证</title>");
		email.append("</head><body>");
		email.append("<p>尊敬的用户：<strong>"+loginName+",</strong></p>");
		email.append("<p>为了能使你的账号成功注册，请尽快点击以下链接！（请在24小时之内完成）</p>");
		email.append("<p><u><a href=\""+url+"\">"+url+"</a></u>   &#13;</p>");
		email.append("</body></html>");
		return email.toString();
	}
	
	public static void main(String[] args) {

	}
}
