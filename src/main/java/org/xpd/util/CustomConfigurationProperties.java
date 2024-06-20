package org.xpd.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="custom-app")
public class CustomConfigurationProperties {

	private String poolId;
	private String appClientId;
	private int otpTimeout;
	private String currentLoginDomain;
	
	public String getCurrentLoginDomain() {
		return currentLoginDomain;
	}
	public void setCurrentLoginDomain(String currentLoginDomain) {
		this.currentLoginDomain = currentLoginDomain;
	}
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	public String getAppClientId() {
		return appClientId;
	}
	public void setAppClientId(String appClientId) {
		this.appClientId = appClientId;
	}
	public int getOtpTimeout() {
		return otpTimeout;
	}
	public void setOtpTimeout(int otpTimeout) {
		this.otpTimeout = otpTimeout;
	}
	
}
