package org.xpd.app.service;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	@Autowired
	RestTemplate restTemplate;
	ObjectMapper mapper = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Override
	public Map<String, Object> getTokenFromUserMgmtApplication(AuthRequest authRequest, String loginUrl) {
		RestTemplate RestTemplate1=new RestTemplate();
		LOGGER.info("Enter In getTokenFromUserMgmtApplication method..");
		System.out.println(loginUrl);
		HttpHeaders headers = new HttpHeaders();
		Map<String,Object> map=new HashMap<String,Object>();
//		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "application/json");
		HttpEntity<AuthRequest> entity = new HttpEntity<AuthRequest>(authRequest, headers);
		AuthResponse authResponse = new AuthResponse();
		Messages messages = new Messages();
		try {
			String restult = RestTemplate1.exchange(loginUrl, HttpMethod.POST, entity, String.class).getBody();
			map = mapper.readValue(restult, new TypeReference<Map<String,Object>>() {
			});
		} catch (Exception exe) {
			messages.setCode("XPD-01");
			messages.setStatus("Fail");
			messages.setDetails("" + exe);
			authResponse.setMessages(messages);
		}
		
		LOGGER.info("Exit from getTokenFromUserMgmtApplication method..");
		return map;
	}
	
	

}
