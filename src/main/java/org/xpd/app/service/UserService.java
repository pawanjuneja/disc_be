package org.xpd.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.xpd.app.controller.UserController;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdCompanyRelationship;
import org.xpd.models.XpdPaymentDateStrategy;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.CognitoPoolDto;
import org.xpd.models.dto.MoreCompanies;
import org.xpd.models.dto.PaymentStrategyDto;
import org.xpd.repo.PaymentStrategyRepo;
import org.xpd.repo.UserRepo;
import org.xpd.repo.XpdCompanyRelationshipRepo;
import org.xpd.repo.XpdCompanyRepo;
import org.xpd.util.CustomConfigurationProperties;
import org.xpd.util.SendInvoiceEmailJob;
import org.xpd.util.SendSMSJob;
import org.xpd.util.SessionUtil;

/**
 * @author Pawan Juneja
 *	This service provides APIs which are related to all the screens of Buyer dashboard.
 * 
 * @implements IBuyerInvoiceService
 * 
 */
@Service
public class UserService implements IUserService {

Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserRepo userRepo;
	@Autowired
	CustomConfigurationProperties poolConfig;

	@Autowired
	PaymentStrategyRepo paymentStrategyRepo;
	
	@Autowired
	XpdCompanyRepo xpdCompanyRepo;
	
	@Autowired
	XpdCompanyRelationshipRepo xpdCompanyRelationshipRepo;
	
	@Override
	public XpdCompany getCompanyName(String username) {
		logger.info("Fetching Company information");
		XpdCompany currentCompany = getCurrentCompany(username);
		XpdCompany company = new XpdCompany();
		company.setId(currentCompany.getId());
		company.setCompanyName(currentCompany.getCompanyName());
		return company;
	}
	
	@Override
	public XpdUser getUserByUsername(String username) {
		logger.info("Fetching user information");
		return this.userRepo.findByUsername(username);
	}
	
	@Override
	public CognitoPoolDto getCognitoPoolConfig() {
		CognitoPoolDto cognitoPoolDto = new CognitoPoolDto();
		cognitoPoolDto.setUserPoolId(poolConfig.getPoolId());
		cognitoPoolDto.setClientId(poolConfig.getAppClientId());
		return cognitoPoolDto;
	}

	@Override
	public boolean sendPasswordChangeEmail(@RequestParam String username) {
		XpdUser user = userRepo.findByUsername(username);
		StringBuffer emailContent = new StringBuffer();
		emailContent.append("Dear "+user.getXpdcompany().getCompanyName()+",<br><br>"
				+ "Your password for Xpedize has been changed successfully.<br><br>"
				+ "We have received a request to have your password reset for "+user.getUsername()+". "
				+ "If you did not make this request, please reach out to us immediately on info@xpedize.com"
				+ " or call on +91 9810325445 / +91 9820670506.<br><br>"
				+ "");
		
//	Sending Email and SMS from here
		SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
		emailJob.setMessage(emailContent.toString());
		emailJob.setToAddress(user.getUsername().toString());
		emailJob.setSubject("DONE ! Your password has been changed successfully.");
		try {
			Thread emailThread = new Thread(emailJob);
			emailThread.start();
		} catch(Exception ee) {
			System.out.println("Error in sending Mail....!");
		}
		
//		SMS
		SendSMSJob smsJob = new SendSMSJob();
		try {
			smsJob.setMessage("Done ! Your password on Xpedize for login ID : "+user.getUsername()+" has been changed successfully."
					);
			smsJob.setTopicARN(user.getArn().toString());
			Thread job = new Thread(smsJob);
			job.start();
			
		} catch(Exception e) {
			System.out.println("Error in Sending SMS");
		}
		return true;
	}
	
	@Override
	public boolean companyIsSupplier(long companyId) {
		List<XpdCompanyRelationship> lstSupplierRelationshipList = xpdCompanyRepo.isSupplierCompany(companyId);
		return (lstSupplierRelationshipList != null && lstSupplierRelationshipList.size()>0);
	}
	
	@Override
	public MoreCompanies getUserCompanies(String username, boolean isSupplier) {
		XpdUser currentUser = userRepo.findByUsername(username);
		MoreCompanies moreCompanies = new MoreCompanies();
		moreCompanies.setLstBuyerCompanies(new ArrayList<XpdCompany>());
		moreCompanies.setLstSupplierCompanies(new ArrayList<XpdCompany>());
		XpdCompany currentCompany = getCurrentCompany(username);
		currentUser.getLinkedCompany().forEach(company -> {
			List<XpdCompanyRelationship> buyerRelationships = xpdCompanyRepo.isBuyerCompany(company.getId());
			if (buyerRelationships != null && !buyerRelationships.isEmpty()) {
				if (currentCompany.getId() != company.getId())
					moreCompanies.getLstBuyerCompanies().add(company);
				else if (isSupplier)
					moreCompanies.getLstBuyerCompanies().add(company);
			}
			
			List<XpdCompanyRelationship> supplierRelationships = xpdCompanyRepo.isSupplierCompany(company.getId());
			if (supplierRelationships != null && !supplierRelationships.isEmpty()) {
				if (currentCompany.getId() != company.getId())
					moreCompanies.getLstSupplierCompanies().add(company);
				else if (!isSupplier)
					moreCompanies.getLstSupplierCompanies().add(company);
			}			
		});
		return moreCompanies;
	}
	
	@Override
	public List<XpdCompany> getAllRelatedCompanies(String username) {
		XpdUser currentUser = userRepo.findByUsername(username);
		XpdCompany currentCompany = getCurrentCompany(username);
		List<XpdCompany> lstResultCompanies = new ArrayList<XpdCompany>();
		List<XpdCompany> lstSuppliers = xpdCompanyRelationshipRepo.getSupplierCompaniesForBuyer(currentCompany.getId());
		List<XpdCompany> lstBuyers = xpdCompanyRelationshipRepo.getBuyersCompaniesForSupplier(currentCompany.getId());
		
		lstSuppliers.forEach(supCompany -> {
			Optional<XpdCompany> comp = lstResultCompanies.stream().filter(company -> company.getId() == supCompany.getId()).findFirst();
			if (!comp.isPresent())
				lstResultCompanies.add(supCompany);
		});
		
		lstBuyers.forEach(buyCompany -> {
			Optional<XpdCompany> comp = lstResultCompanies.stream().filter(company -> company.getId() == buyCompany.getId()).findFirst();
			if (!comp.isPresent())
				lstResultCompanies.add(buyCompany);
		});
		return lstResultCompanies;
	}
	
	@Override
	public List<XpdCompany> getAllUserCompanies(String username) {
		XpdUser currentUser = userRepo.findByUsername(username);
		return currentUser.getLinkedCompany();
	}
	
	@Override
	public List<PaymentStrategyDto> getAllUserCompaniesWithPaymentStrategies(String username) {
		List<PaymentStrategyDto> companyAndStrategies = new ArrayList<PaymentStrategyDto>();
		XpdUser currentUser = userRepo.findByUsername(username);
		currentUser.getLinkedCompany().forEach(company -> {
			PaymentStrategyDto paymentStrategyDto = new PaymentStrategyDto();
			company.getOrgMasters();
			paymentStrategyDto.setCompany(company);
			XpdPaymentDateStrategy strategy = paymentStrategyRepo.findPaymentStrategyForCompany(company.getId());
			if (strategy != null)
				strategy.getCompany().getOrgMasters();
			paymentStrategyDto.setPaymentStrategy(strategy);
			companyAndStrategies.add(paymentStrategyDto);
		});
		return companyAndStrategies;
	}
	
	@Override
	public List<XpdUser> getUsers() {
		return userRepo.findAllActiveUsers();
	}
	
	@Override
	public List<XpdUser> getAllCompanyUsers(String username) {
		List<XpdUser> allUsers = userRepo.findAllActiveUsers();
		List<XpdUser> companyUsers = new ArrayList<XpdUser>();
		List<XpdCompany> relatedCompanies = this.getAllRelatedCompanies(username);
		allUsers.forEach(user -> {
			if (user.getXpdcompany() != null) {
				Optional<XpdCompany> foundCompany = relatedCompanies.stream().filter(company -> company.getId() == user.getXpdcompany().getId()).findFirst();
				if (foundCompany.isPresent()) {
					companyUsers.add(user);
				} else {
					for (XpdCompany linkedCompany : user.getLinkedCompany()) {
						Optional<XpdCompany> foundLinkedCompany = relatedCompanies.stream().filter(company -> company.getId() == linkedCompany.getId()).findFirst();
						if (foundLinkedCompany.isPresent()) {
							companyUsers.add(user);
							break;
						}
					}
				}
			}
		});
		
		return companyUsers;
	}
	
	
	@Override
	public boolean setCurrentCompany(String username, Long companyId) {
		try {
			XpdCompany company = xpdCompanyRepo.findOne(companyId);
			SessionUtil.getInstance().userCompanyMap.put(username+"_company", company);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public XpdCompany getCurrentCompany(String username) {
		return SessionUtil.getInstance().userCompanyMap.get(username+"_company");
	}
}
