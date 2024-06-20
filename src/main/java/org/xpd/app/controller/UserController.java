package org.xpd.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpd.app.service.AuthRequest;
import org.xpd.app.service.AuthenticationServiceImpl;
import org.xpd.app.service.UserService;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.CognitoPoolDto;
import org.xpd.models.dto.MoreCompanies;
import org.xpd.models.dto.PaymentStrategyDto;
import org.xpd.repo.UserRepo;
import org.xpd.repo.XpdCompanyRepo;
import org.xpd.util.CustomConfigurationProperties;
import org.xpd.util.Md5_Encryt;
import org.xpd.util.SessionUtil;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(origins = "*")
public class UserController {
	@Value("${login_api_url}")
	String loginUrl="";
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserRepo userRepo;
	@Autowired
	CustomConfigurationProperties poolConfig;

	@Autowired
	XpdCompanyRepo xpdCompanyRepo;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/company")
	public XpdCompany getCompanyName(String username) {
		return userService.getCompanyName(username);
	}

	@RequestMapping(value = "/getUser")
	public XpdUser getUserByUsername(String username) {
		return userService.getUserByUsername(username);
	}

	@RequestMapping(value = "/saveUser")
	public boolean saveUser(HttpServletRequest request, @RequestParam String username, @RequestBody XpdUser user) {
		try {
			user.setCreatedBy(userRepo.findByUsername(username).getId());
			user.setCreatedDate(new Date());
			user.setIsActive(true);
			this.userRepo.save(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/deleteUser")
	public boolean deleteUser(HttpServletRequest request, @RequestParam String username,
			@RequestBody List<XpdUser> users) {
		try {
			List<Long> userIDs = new ArrayList<Long>();
			users.forEach(user -> {
				userIDs.add(user.getId());
			});
			this.userRepo.inactiveUsers(userIDs);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@RequestMapping(value = "/preLogin")
	public String userLogin(@RequestBody AuthRequest authRequest) { 
		
		authRequest.setApplicationName("invoicemanager");
		AuthenticationServiceImpl service = new AuthenticationServiceImpl();
		Map<String, Object> responseMap= service.getTokenFromUserMgmtApplication(authRequest, loginUrl);
		String auth = ""+responseMap.get("token");
		if(!auth.equals("null")) {
			return auth;
		} else {
			return "null";
		}
	}

	@RequestMapping(value = "/login")
	public Boolean login(HttpServletRequest request, String username) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null)
			ipAddress = request.getRemoteAddr();
		XpdUser user = userRepo.findByUsername(username);
		if (user != null) {
			SessionUtil.getInstance().userSessionMap.put(request.getHeader("Authorization"),
					userRepo.findByUsername(username));
			SessionUtil.getInstance().userSessionMap.put(username, user);
			SessionUtil.getInstance().userCompanyMap.put(username + "_company", user.getXpdcompany());
			logger.info("Login into system with username " + username + " on IP Address: " + ipAddress);
			return true;
		} else {
			logger.info("Unable to find any user with username: " + username);
			return false;
		}
	}

	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request, String username) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null)
			ipAddress = request.getRemoteAddr();
		logger.info(username + " logged out successfully " + username + " on IP Address: " + ipAddress);
		SessionUtil.getInstance().userSessionMap.remove(request.getHeader("Authorization"));
		SessionUtil.getInstance().userSessionMap.remove(username);
		SessionUtil.getInstance().userCompanyMap.remove(username + "_company");
	}

	@RequestMapping(value = "/userPoolConfig")
	public CognitoPoolDto getCognitoPoolConfig() {
		return userService.getCognitoPoolConfig();
	}

	@RequestMapping(value = "/forgotPassword")
	public boolean sendPasswordChangeEmail(@RequestParam String username) {
		return userService.sendPasswordChangeEmail(username);
	}

	@RequestMapping(value = "/companyIsSupplier")
	public boolean companyIsSupplier(long companyId) {
		return userService.companyIsSupplier(companyId);
	}

	@RequestMapping(value = "/getUserCompanies")
	public MoreCompanies getUserCompanies(HttpServletRequest request, String username, boolean isSupplier) {
		return userService.getUserCompanies(username, isSupplier);
	}

	@RequestMapping(value = "/getAllUserCompanies")
	public List<XpdCompany> getAllUserCompanies(HttpServletRequest request, String username) {
		return userService.getAllUserCompanies(username);
	}

	@RequestMapping(value = "/getAllRelatedCompanies")
	public List<XpdCompany> getAllRelatedCompanies(HttpServletRequest request, String username) {
		return userService.getAllRelatedCompanies(username);
	}

	@RequestMapping(value = "/getAllUserCompaniesWithPaymentStrategies")
	public List<PaymentStrategyDto> getAllUserCompaniesWithPaymentStrategies(HttpServletRequest request,
			String username) {
		return userService.getAllUserCompaniesWithPaymentStrategies(username);
	}

	@RequestMapping(value = "/setCurrentCompany")
	public boolean setCurrentCompany(HttpServletRequest request, String username, Long companyId) {
		return userService.setCurrentCompany(username, companyId);
	}

	@RequestMapping(value = "/getCurrentCompany")
	public XpdCompany getCurrentCompany(HttpServletRequest request, String username) {
		return userService.getCurrentCompany(username);
	}

	@RequestMapping(value = "/getUsers")
	public List<XpdUser> getUsers(HttpServletRequest request, String username) {
		XpdUser user = userRepo.findByUsername(username);
		if (user.getIsXpdUser())
			return userService.getUsers();
		else
			return userService.getAllCompanyUsers(username);
	}

	public XpdCompany getCurrentCompany(String username) {
		return SessionUtil.getInstance().userCompanyMap.get(username + "_company");
	}

//	Save Password
	@RequestMapping(value = "/savePwd")
	public boolean savePasswordForUser(HttpServletRequest request, String username, String pwd) {
		logger.info("savePasswordForUser() : Save Password Called for User: " + username);
		XpdUser user = userRepo.findByUsername(username);
		if (user != null) {
			String oldPwd = user.getPassword();
			if (oldPwd == null || oldPwd.equals("")) {
				oldPwd = "";
				logger.info("savePasswordForUser() : Old Password found is 'NULL' or 'Empty' for User: " + username);
			}
			String newPwd = Md5_Encryt.getMd5(pwd);

			if (oldPwd.equals(newPwd)) {
				logger.info("savePasswordForUser() : Old Password and New Password is same for User: " + username);
				logger.info("Not Updating Password for User: " + username);
				return true;
			} else {
				user.setPassword(newPwd);
				userRepo.save(user);
				logger.info("savePasswordForUser() : New Password detected, saving password for User: " + username);
				return true;
			}

		} else {
			logger.info("savePasswordForUser() : No user found for username: " + username);
			return false;
		}
	}

}
