package org.xpd.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	@SuppressWarnings("static-access")
	public static boolean sendEmail(String toEmailAddress, String content, String subject) {    
	      String from = "notifications@xpedize.com";
	      String host = "smtp.gmail.com";
	      Properties props = System.getProperties();
	      props.setProperty("mail.smtp.host", host);
	      props.setProperty("mail.transport.protocol", "smtp");   
	      props.setProperty("mail.smtp.starttls.enable", "true");  
	      props.setProperty("mail.smtp.auth", "true");   
	      props.setProperty("mail.smtp.port", "587");   
	      Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	    	    protected PasswordAuthentication getPasswordAuthentication() {
	    	        return new PasswordAuthentication("notifications@xpedize.com", "XPD@notifications");
	    	    }
	    	});
	      try {
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
	         message.setSubject(subject);
	         message.setText(content);
	         Transport trans = session.getTransport("smtp");
	         trans.send(message);
	         System.out.println("Sent Email successfully....");
	         return true;
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	         return false;
	      }
	   }
	public static void main(String[] args) {
		String emailContent = "Hi Pawan, \n\n"
				+ "Invoice offer has been raised at 1.25% (30 days) discount\n"
						+ "\n"
						+ "Thanks,\n"
						+ "Team Xpedize";
		EmailUtil.sendEmail("pwnjuneja@gmail.com", emailContent, "Congratulations! Got Invoice Offer..");
	}
}