package org.xpd.util;

import java.util.HashMap;
import java.util.Map;

import org.xpd.models.XpdCompany;
import org.xpd.models.XpdUser;

public class SessionUtil {

	public Map<String, XpdUser> userSessionMap = new HashMap<String, XpdUser>();
	
	public Map<String, XpdCompany> userCompanyMap = new HashMap<String, XpdCompany>();
	
	public Map<String, OTPDetails> userOTPMap = new HashMap<String, OTPDetails>();
	
	private static SessionUtil _sessionUtil;
	
	private SessionUtil() {
	}
	
	public static SessionUtil getInstance() {
		if (_sessionUtil == null)
			_sessionUtil = new SessionUtil();
		
		return _sessionUtil;
	}
	
}
