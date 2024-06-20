package org.xpd.util;

public class SendSMSJob implements Runnable {
	private String topicARN;
	private String message;
	
	@Override
	public void run() {
		try {
			AWS_SMS_Util.sendSMSMessage(message, topicARN);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void setTopicARN(String arn) {
		this.topicARN = arn;		
	}
	
	public void setMessage(String msg) {
		this.message = msg;
	}
	

}
