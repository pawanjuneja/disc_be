package org.xpd.util;

public class SendEmailJob implements Runnable {

	private String emailContent;
	private String emailId;
	private String emailSubject;
	
	@Override
	public void run() {
		EmailUtil.sendEmail(emailId, emailContent, emailSubject);
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public void setEmailSubject(String subject) {
		this.emailSubject = subject;
	}

	
}
