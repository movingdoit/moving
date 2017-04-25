package org.base.component.email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Encoder;

public class SimpleMailSender
{
	
   private Logger  log = Logger.getLogger(SimpleMailSender.class);
  public boolean sendTextMail(Email mailInfo) throws Exception
  {
    MyAuthenticator authenticator = null;
    Properties pro = mailInfo.getProperties();
    if (mailInfo.isValidate())
    {
      authenticator = new MyAuthenticator(mailInfo.getUserName(), 
        mailInfo.getPassword());
    }

    Session sendMailSession = 
      Session.getDefaultInstance(pro, authenticator);
    try
    {
      Message mailMessage = new MimeMessage(sendMailSession);

      //Address from = new InternetAddress(mailInfo.getFromAddress());
      //设置自定义发件人昵称   
      String nick=javax.mail.internet.MimeUtility.encodeText(mailInfo.getEmailSender(), "UTF-8", "B");  
      mailMessage.setFrom(new InternetAddress(nick+"<"+mailInfo.getFromAddress()+">"));  

      //mailMessage.setFrom(from);

      Address to = new InternetAddress(mailInfo.getToAddress());
      mailMessage.setRecipient(Message.RecipientType.TO, to);

      mailMessage.setSubject(mailInfo.getSubject());

      mailMessage.setSentDate(new Date());

      String mailContent = mailInfo.getContent();
      mailMessage.setText(mailContent);

      Transport.send(mailMessage);
      return true;
    } catch (MessagingException ex) {
    	throw ex;
    }catch (Exception ex){
    	throw ex;
    }
  }

  public static boolean sendHtmlMail(Email mailInfo)throws Exception
  {
    MyAuthenticator authenticator = null;
    try {
      Properties pro = mailInfo.getProperties();

      if (mailInfo.isValidate()) {
        authenticator = new MyAuthenticator(mailInfo.getUserName(), 
          mailInfo.getPassword());
      }

      Session sendMailSession = 
        Session.getDefaultInstance(pro, authenticator);

      Message mailMessage = new MimeMessage(sendMailSession);

      InternetAddress from = null;
      try {
        from = new InternetAddress(mailInfo.getFromAddress());
        from.setPersonal(mailInfo.getEmailSender()+"<" + mailInfo.getFromAddress() + ">");
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

      mailMessage.setFrom(from);

      Address to = new InternetAddress(mailInfo.getToAddress());

      mailMessage.setRecipient(Message.RecipientType.TO, to);

      mailMessage.setSubject(mailInfo.getSubject());

      mailMessage.setSentDate(new Date());

      Multipart mainPart = new MimeMultipart();

      BodyPart html = new MimeBodyPart();

      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
      mainPart.addBodyPart(html);

      mailMessage.setContent(mainPart);

      Transport.send(mailMessage);
      return true;
    } catch (MessagingException ex) {
      throw ex;
    }
  }

  public void send(Email mailInfo)throws Exception
  {
    MyAuthenticator authenticator = null;
    Properties pro = mailInfo.getProperties();

    if (mailInfo.isValidate()) {
      authenticator = new MyAuthenticator(mailInfo.getUserName(), 
        mailInfo.getPassword());
    }

    Session session = Session.getDefaultInstance(pro, authenticator);

    session.setDebug(true);

    MimeMessage message = new MimeMessage(session);
    try
    {
      message.setFrom(new InternetAddress(mailInfo.getFromAddress()));

      message.addRecipient(Message.RecipientType.TO, 
        new InternetAddress(mailInfo.getToAddress()));

      message.setSubject(mailInfo.getSubject());

      Multipart multipart = new MimeMultipart();

      BodyPart contentPart = new MimeBodyPart();
      contentPart.setText(mailInfo.getContent());
      multipart.addBodyPart(contentPart);

      BodyPart messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(mailInfo.getAffix());

      messageBodyPart.setDataHandler(new DataHandler(source));

      BASE64Encoder enc = new BASE64Encoder();
      messageBodyPart.setFileName("=?GBK?B?" +enc.encode(mailInfo.getAffixName().getBytes()) + "?=");
      multipart.addBodyPart(messageBodyPart);

      message.setContent(multipart);

      message.saveChanges();

      Transport transport = session.getTransport("smtp");

      transport.connect(mailInfo.getMailServerHost(), 
        mailInfo.getUserName(), mailInfo.getPassword());

      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    } catch (Exception e) {
        throw e;
    }
  }
  
  /**
	 * 以http方式（调用第三方系统接口）发送邮件
	 * @param url  请求地址
	 * @param postData 文本格式
	 * @return
	 * @throws Exception
	 */
	public  Document sendHttpPostXML(String url,String postData)throws Exception{
		URL dataurl;
		Document doc=null;
		try {
			dataurl = new URL(url);
			HttpURLConnection con=(HttpURLConnection)dataurl.openConnection();
			con.setRequestMethod("POST");
		    con.setRequestProperty("Connection", "Keep-Alive");
		    con.setDoOutput(true);
		    con.setDoInput(true);
		    OutputStreamWriter out=new OutputStreamWriter(con.getOutputStream());
		    String str=String.format(Locale.CHINA,postData);
	        out.write(str);
	        out.flush();
	        out.close();
		    
	        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf8"));  
	        SAXReader saxReader =new SAXReader();
	        doc = saxReader.read(reader); //读取XML文件,获得document对象
	        reader.close();
		    con.disconnect();
		} catch (MalformedURLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} 
		return doc;
	}  

}