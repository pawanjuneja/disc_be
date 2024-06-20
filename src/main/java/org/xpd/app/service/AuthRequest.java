package org.xpd.app.service;
/**
 * @author Siddhant Rao
 *
 */
public class AuthRequest  {
	private String userName;
    private String password;
    private String applicationName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
 
}
