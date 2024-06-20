package org.xpd.app.service;

import java.util.List;

import org.xpd.models.XpdCompany;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.CognitoPoolDto;
import org.xpd.models.dto.MoreCompanies;
import org.xpd.models.dto.PaymentStrategyDto;

/**
 * This interface is created to generate the implementation for Buyer. These are the minimum
 * 		methods which user needs to implement to get the Buyer screens to work.  
 * 
 * @author Pawan Juneja
 *
 */
public interface IUserService {
	
	XpdCompany getCompanyName(String username);
	
	XpdUser getUserByUsername(String username);
	
	CognitoPoolDto getCognitoPoolConfig();

	boolean sendPasswordChangeEmail(String username);
	
	boolean companyIsSupplier(long companyId);
	
	MoreCompanies getUserCompanies(String username, boolean isSupplier);
	
	boolean setCurrentCompany(String username, Long companyId);

	List<XpdCompany> getAllUserCompanies(String username);

	List<PaymentStrategyDto> getAllUserCompaniesWithPaymentStrategies(String username);

	List<XpdUser> getUsers();

	List<XpdCompany> getAllRelatedCompanies(String username);

	List<XpdUser> getAllCompanyUsers(String username);

}
