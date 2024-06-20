package org.xpd.util;

import javax.mail.MessagingException;

public class SendInvoiceEmailJob implements Runnable{
	private String toAddress;
	private String subject;
	private String message;
	
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getToAddress() {
		return this.toAddress;
	}
	
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void run() {
		try {
			
			InvoiceEmailUtil.sendInvoiceEmail(toAddress, subject, message);
		
		} catch (MessagingException e) {
		
			e.printStackTrace();
			
		}
		
	}

}
