package org.xpd.util;

import java.util.Date;

public class OTPDetails {

	private Integer otp;
	
	private Date timeOfGeneration;

	public OTPDetails(Integer otp, Date timeOfGeneration) {
		super();
		this.otp = otp;
		this.timeOfGeneration = timeOfGeneration;
	}

	public Integer getOtp() {
		return otp;
	}

	public Date getTimeOfGeneration() {
		return timeOfGeneration;
	}
	

}
