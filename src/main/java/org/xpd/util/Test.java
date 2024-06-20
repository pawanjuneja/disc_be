package org.xpd.util;

public class Test {
	public static void main(String args[]) {
//			SendSMSJob smsJob = new SendSMSJob();
//			smsJob.setMessage("xpd sms");
//			smsJob.setTopicARN("arn:aws:sns:ap-southeast-1:561503721240:xpd-Vishal");
//			Thread job = new Thread(smsJob);
//			job.start();
		
		SendInvoiceEmailJob InvJob = new SendInvoiceEmailJob();
		InvJob.setMessage("<p style='color: red'>This is simple red color paragraph</p>");
		InvJob.setSubject("Demo Mail");
		InvJob.setToAddress("vishuahlawat01@gmail.com");
		Thread job = new Thread(InvJob);
		job.start();
	}

}
