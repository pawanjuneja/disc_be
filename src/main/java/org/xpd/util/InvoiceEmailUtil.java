package org.xpd.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class InvoiceEmailUtil {
	
	static final String signature = ""
			+ "Thanking you,<br>"
			+ "Team Xpedize"
			+ "<br><br>"
			+ "info@xpedize.com | +91 9810325445 | +91 9820670506<br>"
			+ "Xpedize Ventures Pvt Ltd | One Horizon Centre, Level 18 |Sector 43, Gurugram - 122002<br><br>"
			+ "P.S.: This is a system generated email. Please do not reply to this email.<br><br>"
			+ "<p style='font-size: 12px'>"
			+ "Disclaimer :<br>"
			+ "---------------------------------------------------------------------<br>"
			+ "This email was sent from Xpedize. The contents of this email, including the attachments, "
			+ "are LEGALLY PRIVILEGED AND CONFIDENTIAL to the intended recipient at the email address to "
			+ "which it has been addressed. If you receive it in error, please notify the sender "
			+ "immediately by return email on info@xpedize.com and then permanently delete it from your system."
			+ " The unauthorized use, distribution, copying or alteration of this email, including the attachments, "
			+ "is strictly forbidden. Thank you. Please note that neither Xpedize Ventures Private Limited "
			+ "nor the sender accepts any responsibility for viruses and it is your responsibility to scan "
			+ "the email and attachments ( if any )"
			+ "</p>";
	
	public static void sendInvoiceEmail(String toAddress,String subject, String message) throws AddressException, MessagingException{
		
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "notifications@xpedize.com";
        String password = "XPD@notifications";
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailFrom, password);
            }
        };
        
 
        Session session = Session.getInstance(properties, auth);
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(mailFrom));
//        If any user reply to this, replied email will be directed to this email address...( One of our company email address ie info@xpedize.com )
        Address add[] = new Address[1];
        add[0]=new InternetAddress("info@xpedize.com");
        
        msg.setReplyTo(add);
        message = message + signature;
        
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        InternetAddress[] mailCopy = {new InternetAddress("info@xpedize.com")};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setRecipients(Message.RecipientType.BCC, mailCopy);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set text/html message
        
        msg.setContent(message, "text/html");
 
        // sends the e-mail
        Transport.send(msg);
        System.out.println("Email Sent to "+toAddress +" and a copy is sent to "+mailCopy[0]);
        
 
    }
 
    /**
     * Test the send html e-mail method
     *
     */
    public static void main(String[] args) {
        // SMTP server information

 
        // outgoing message information
        String mailTo = "vishuahlawat01@gmail.com";
        String subject = "Success ! Early Payment Request Submitted";
 
        // message contains HTML markups
        String message = "<i>Dear <b>Hero Moto Corp</b></i> !<br><br>";
        message += "<b>Offer</b> has been raised on <span style='color: red'><b>3</b></span> invoices at Percentage <span style='color: green'><b><u>4.2 %</u></b></span> and New Payment Date is <span style='color: blue'><b>12-Oct-2018</b></span><br><br>";
        message += "<table style='border: 1px solid black;border-collapse: collapse;width: 100%;text-align: center;'>"
        		+ "<tr style='background-color: lightgrey'>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse'>Invoice Number</th>"
//        		+ "<th></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse'>Amount Due</th>"
//        		+ "<th></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse'>Discount Amount</th>"
//        		+ "<th></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse'>Net Amount</th>"
//        		+ "<th></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse'>Original Payment Date</th>"
        		+ "</tr>"
        		+ ""
        		+ "<tr>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INV-01</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 2200</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 320</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 1880</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>12-Nov-2018</td>"
        		+ "</tr>"
        		+ ""
        		+ "<tr>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INV-02</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 1000</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 100</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 900</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>12-Nov-2018</td>"
        		+ "</tr>"
        		+ ""
        		+ "<tr>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INV-03</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 1000</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 200</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>INR 800</td>"
//        		+ "<td></td>"
        		+ "<td style='border: 1px solid black;border-collapse: collapse'>12-Nov-2018</td>"
        		+ "</tr>"
        		+ ""
        		+ "</table>"
        		+ ""
        		+ "<br>"
        		+ "<br>"
        		+ "The above invoices have been successfully submitted and currently under review with the buyer. We will share with you the final approval update once confirmed at the buyers end."
        		+ "<br>"
        		+ "Thanking you,"
        		+ "<br><br>"
        		+ "Team <font color=blue>Xpedize</font>."
        		+ "<br><br>";
        message += "P.S.: <font color=red>This is a system generated email. Please do not reply to this email.</font>";
 
 
        try {
        	InvoiceEmailUtil.sendInvoiceEmail(mailTo, subject, message);            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
    }

}
