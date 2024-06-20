package org.xpd.app.service;

import java.io.Serializable;
public class AuthResponse  implements Serializable{

 
    /**
	 * Siddhant Rao
	 */
	private static final long serialVersionUID = 1L;

	private Boolean authenticated;
  
    private String userName;
  
  
    private String emailId;
   
    private String userFullName;
  
    private String token;
    

    private Messages messages;

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Messages getMessages() {
		return messages;
	}

	public void setMessages(Messages messages) {
		this.messages = messages;
	}
	
	public AuthResponse() {
		
		
	}

	public AuthResponse(Boolean authenticated, String userName, String applicationName, String emailId,
			String userFullName, String token, Messages messages) {
		super();
		this.authenticated = authenticated;
		this.userName = userName;
		
		this.emailId = emailId;
		this.userFullName = userFullName;
		this.token = token;
		this.messages = messages;
	}

	
    
    
    
    
    
}