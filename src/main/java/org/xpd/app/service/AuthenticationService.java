package org.xpd.app.service;

import java.util.Map;

public interface AuthenticationService {
	
	public Map<String, Object> getTokenFromUserMgmtApplication(AuthRequest authRequest, String loginUrl);

}
 