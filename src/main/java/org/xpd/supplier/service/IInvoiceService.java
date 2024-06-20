package org.xpd.supplier.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdInvoice;
import org.xpd.models.XpdOffer;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.dto.XpdOfferDto;
import org.xpd.supplier.dto.InvoiceDto;
import org.xpd.supplier.dto.InvoiceOfferDto;
import org.xpd.supplier.dto.SupplierDashboardDto;

/**
 * This interface is created to generate the implementation for supplier. These are the minimum
 * 		methods which user needs to implement to get the supplier screen to work.  
 * 
 * @author Pawan Juneja
 *
 */
public interface IInvoiceService {

	/**
	 * This method should return all locations of company. 
	 * 
	 * @param username Logged in username
	 * @return
	 */
	List<XpdOrgMaster> getCompanyLocations(String username);

	/**
	 * This method is to fetch all the open invoices on which an offer can be raised. 
	 * 	Payment due date for all these invoices should be later than the new payment date
	 * 	which is given as per company standard
	 * 
	 * @param currentLocationId Selected location to fetch invoices. 0 for all
	 * @param buyerCompanyId 
	 * @param username Logged in username
	 * @return List of {@link InvoiceDto}
	 */
	List<InvoiceDto> getLocationOpenInvoices(long currentLocationId, long buyerCompanyId, String username);

	/**
	 * This method is used to return the data which is to be shown on top 3 dashboard boxes
	 * 	which are shown on Supplier screen.
	 * 
	 * @param username Logged in username
	 * @return {@link SupplierDashboardDto} Which has all placeholders for dashboard boxes
	 */
	SupplierDashboardDto getSupplierDashboardData(String username, long BuyerId);

	
	/**
	 * This method is submitting offer for invoices.
	 * 
	 * @param {@link InvoiceOfferDto} This Dto contains list of invoices and offer details
	 * @return true/false
	 */
	boolean submitInvoiceOffer(InvoiceOfferDto invoiceOfferDto);

	
	/**
	 * This method will fetch the open invoices for all company irrespective of any specific location
	 * 
	 * @param username Logged in username
	 * @return List of {@link InvoiceDto}
	 * @throws ParseException 
	 */
	List<InvoiceDto> getAllCompanyOpenInvoices(String username) throws ParseException;

	/**
	 * This method fetches the offer which is specified for a particular company. It can be from Buyer
	 * 	also. Returning new offer if no offer is saved for any company. 
	 * 
	 * @param username Logged in username
	 * @param buyerId Buyer location ID
	 * @return {@link XpdOffer}
	 * @throws ParseException 
	 */
	XpdOfferDto getCompanyInvoiceOffer(String username, long buyerId) throws ParseException;

	/**
	 * This method is to fetch all the invoices which are raised an offer and are in progress
	 * 
	 * @param currentLocationId selected location to fetch invoices. 0 for all locations
	 * @param username Logged in username
	 * @return
	 */
	List<InvoiceDto> getLocationUnderClearingInvoices(long buyerId, long currentLocationId, String username);

	/**
	 * This method is to fetch all the invoices which are Paid by Buyer. On all the invoices for 
	 * 	which an offer has been raised earlier and successfully approved by buyer and paid by Buyer
	 * 
	 * @param username Logged in username
	 * @param submissionDate Filter Date on which the the invoice has been submitted by Supplier 
	 * @param fromDate Payment start date for to filter the invoices.
	 * @param toDate Payment end date for payment to filter the invoices.
	 * @return List of {@link InvoiceDto}
	 * @throws ParseException
	 */
	List<InvoiceDto> getApprovedInvoices(String username,String buyerId, String submissionDate, String fromDate, String toDate, String invoiceNumber) throws ParseException;

	/**
	 * This method should return OTP which is generated to raise an offer. This OTP is sent to 
	 * 	user's mobile number and company's email id. This OTP is valid for a specific time which 
	 * 	is configurable.
	 * 
	 * @param username Logged in username
	 * @return true/false
	 */
	boolean generateOTP(String username);

	/**
	 * This method is checking if the OTP which is entered by user is valid or not. 
	 * 
	 * @param username Logged in username
	 * @param otp Submitted OTP
	 * @return true/false
	 */
	boolean checkOTPIsValid(String username, String otp);
	
	boolean getAuth(String username);
	
	boolean checkPAN(String username, String nPAN);
	
	boolean saveMobileNumber(String username, String Mob);
	
	boolean checkUserRole(String username);

	List<XpdOrgMaster> getBuyerLocations(String username);
	
	boolean sendPasswordChangeEmail(String username);
	
	boolean companyIsBuyer(String username);
	
//	Get Buyer Companies
	List<XpdCompany> getBuyerCompanies(String username);

	Date getNewPaymentDate(long companyId);
	
//	Get Specific Buyer Company Locations
//	List<XpdOrgMaster> getSpecificBuyerCompanyLocations(String username, long buyerId);
	

	
}

