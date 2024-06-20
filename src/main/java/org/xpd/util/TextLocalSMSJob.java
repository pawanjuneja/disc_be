package org.xpd.util;

public class TextLocalSMSJob implements Runnable{
	private String mobileNumber;
	private String message;
	
	@Override
	public void run() {
		Text_Local_SMS_Util.sendSMS(mobileNumber, message);
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
