package org.xpd.buyer.service;

import java.text.ParseException;
import java.util.List;

import org.xpd.models.SysOfferParameter;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdInvoice;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.dto.XpdCompanyOfferDto;
import org.xpd.models.dto.XpdInvoiceLight;
import org.xpd.supplier.dto.ApproveRejectRequestDto;
import org.xpd.supplier.dto.BuyerDashboardDto;
import org.xpd.supplier.dto.InvoiceDto;
import org.xpd.supplier.dto.InvoiceOfferDto;

/**
 * This interface is created to generate the implementation for Buyer. These are the minimum
 * 		methods which user needs to implement to get the Buyer screens to work.  
 * 
 * @author Pawan Juneja
 *
 */
public interface IBuyerInvoiceService {

	/**
	 * This method is implemented to find out all the locations of selected buyer. 
	 * 
	 * @param username Fetch company detail from logged in user name 
	 * @return List of {@link XpdOrgMaster}(Company locations)
	 */
	List<XpdOrgMaster> getBuyerCompanyLocations(String username);

	
	/**
	 * This method is used to find out top 3 dashboard boxes data.
	 * 
	 * @param username Logged in username
	 * @return {@link BuyerDashboardDto}-It contains all the information which needs to be shown on all 3 dashboard boxes
	 * @throws Exception 
	 */
	BuyerDashboardDto getBuyerDashboardData(String username) throws Exception;

	
	/**
	 * This method is used to find out all the invoices which are in open/rejected state. 
	 * 
	 * @param currentLocationId Selected location for which the invoices needs to be fetched
	 * @param username Logged in username
	 * @return List of {@link InvoiceDto}
	 */
	List<InvoiceDto> getAmountDueLocationInvoices(long currentLocationId, String username);

	
	/**
	 * This method is used to find out all the invoices on which offer was raised and successfully Paid by Buyer.
	 * 
	 * @param location Selected Location from screen
	 * @param username Logged in username
	 * @param supplier Supplier name to search specific invoices
	 * @param fromDate Date from which the invoices should be loaded. This is Payment Date
	 * @param toDate Date till which the invoices should be loaded. This is Payment Date
	 * @return List of {@link InvoiceDto}
	 * @throws ParseException Throwing exception if the date is not parsable
	 */
	List<InvoiceDto> getPaidInvoices(String location, String username, String supplier, String fromDate, String toDate, String invoiceNumber)  throws ParseException ;

	/**
	 * This method is used to find out all the invoice which are in open/rejected state irrespective of any location
	 * 
	 * @param currentLocationId selected location which is 0 to find for all locations
	 * @param username Logged in username
	 * @return List of {@link InvoiceDto}
	 */
	List<InvoiceDto> getBuyerOpenInvoices(long currentLocationId, String username);

	/**
	 * This method is used to find out all the invoices which are in Pending state. On all these invoices an offer has been raised and is not closed. 
	 * 
	 * @param currentLocationId Selected location. 0 to find out from all locations
	 * @param username Logged in username
	 * @return List of {@link InvoiceDto}
	 */
	List<InvoiceDto> getApprovalPendingInvoices(long currentLocationId, String username);
	
	/**
	 * Here Buyer has sent invoices to Approve on which an offer has been raised by Supplier
	 * 
	 * @param approveRejectRequestDto This Dto contains list of invoices, approve/reject information and logged in username
	 * @return
	 */
	boolean approveInvoices(ApproveRejectRequestDto approveRejectRequestDto);
	
	/**
	 * Here Buyer has sent invoices to Reject on which an offer has been raised by Supplier
	 * 
	 * @param approveRejectRequestDto This Dto contains list of invoices, approve/reject information and logged in username
	 * @return
	 */
	boolean rejectInvoices(ApproveRejectRequestDto approveRejectRequestDto);

	/**
	 * This method is used to find out the summary of invoices as per given date. It should fetch all the invoices which are uploaded 
	 * 		between fromDate and toDate
	 * 
	 * @param username Logged in username
	 * @param fromDate Start Date
	 * @param toDate End Date
	 * @return List of {@link XpdInvoiceLight}
	 * @throws ParseException Throwing exception if given date is not correct
	 */
	List<XpdInvoiceLight> getBuyerSummaryInvoices(String username, String fromDate, String toDate) throws ParseException;
	
	boolean companyIsSupplier(String username);
	
	public List<XpdCompanyOfferDto> getSuppliers(String username);
	
	public boolean saveSupplierOffers(String username, Long offerTypeId, List<XpdCompany> supplierCompanies, Double constantPercentage, Double minPercentage, Double maxPercentage, String offerStartDate, String offerEndDate) throws Exception;


	List<InvoiceDto> getUnderClearingInovoices(String username);


	boolean postOnERPInvoices(ApproveRejectRequestDto approveRequestDto, String comments);


	boolean payInvoices(ApproveRejectRequestDto postedOnERPDto, String comments);


	List<SysOfferParameter> getAllOfferTypes();

}
