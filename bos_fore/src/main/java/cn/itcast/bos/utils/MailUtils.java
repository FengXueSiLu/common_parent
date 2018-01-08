package cn.itcast.bos.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.qq.com";
	private static String username = "1293242749@qq.com";
	private static String password = "pnismjdqhopujhjj";
	private static String from = "1293242749@qq.com";
	public static String activeUrl = "http://localhost:9003/bos_fore/customer_activeMail";

	public static void sendMail(String subject, String content, String to){
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			
			// 开启SSL加密，否则会失败
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
			
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

	public static void main(String[] args){
		sendMail("测试邮件", "你好，传智播客", "1293242749@qq.com");
	}
}
