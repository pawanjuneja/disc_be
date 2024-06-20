package org.xpd.supplier.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xpd.app.controller.UserController;
import org.xpd.models.SysOfferType;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdCompanyOffer;
import org.xpd.models.XpdCompanyRelationship;
import org.xpd.models.XpdHoliday;
import org.xpd.models.XpdInvoice;
import org.xpd.models.XpdInvoiceOffer;
import org.xpd.models.XpdInvoiceStatus;
import org.xpd.models.XpdOffer;
import org.xpd.models.XpdOfferCriteria;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.XpdPaymentDateStrategy;
import org.xpd.models.XpdStatus;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.CurrencyDto;
import org.xpd.models.dto.XpdInvoiceLight;
import org.xpd.models.dto.XpdInvoiceNoteLight;
import org.xpd.models.dto.XpdOfferDto;
import org.xpd.repo.DateRepo;
import org.xpd.repo.HolidaysRepo;
import org.xpd.repo.InvoiceNotesRepo;
import org.xpd.repo.InvoiceOfferRepo;
import org.xpd.repo.InvoiceRepo;
import org.xpd.repo.InvoiceStatusRepo;
import org.xpd.repo.OrgMasterRepo;
import org.xpd.repo.PaymentStrategyRepo;
import org.xpd.repo.UserRepo;
import org.xpd.repo.XpdCompanyOfferRepo;
import org.xpd.repo.XpdCompanyRelationshipRepo;
import org.xpd.repo.XpdCompanyRepo;
import org.xpd.repo.XpdOfferCriteriaRepo;
import org.xpd.repo.XpdOfferRepo;
import org.xpd.supplier.dto.InvoiceDto;
import org.xpd.supplier.dto.InvoiceOfferDto;
import org.xpd.supplier.dto.NoteTypeEnum;
import org.xpd.supplier.dto.SupplierDashboardDto;
import org.xpd.util.AppConstants;
import org.xpd.util.CustomConfigurationProperties;
import org.xpd.util.InvoiceUtil;
import org.xpd.util.OTPDetails;
import org.xpd.util.SendEmailJob;
import org.xpd.util.SendInvoiceEmailJob;
import org.xpd.util.SendSMSJob;
import org.xpd.util.SessionUtil;
import org.xpd.util.TextLocalSMSJob;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

@Service
public class InvoiceService implements IInvoiceService {

	Logger logger = LoggerFactory.getLogger(InvoiceService.class);
	
	/**
	 * Invoice Repository. it contains all methods related to fetching invoices for Buyer and Supplier
	 * 
	 */
	@Autowired
	InvoiceRepo invoiceRepo;
	
	/**
	 * Company Repository. It contains database API to work with XpdCompany entity
	 * 
	 */
	@Autowired
	XpdCompanyRepo xpdCompanyRepo;
	
	/**
	 * User Repository. This repository used to work with XpdUser entity.  
	 * 
	 */
	@Autowired
	UserRepo userRepo;
	
	/**
	 * Org Master Repository. This repository used to work with XpdOrgMaster entity. XpdOrgMaster are children of XpdCompany records
	 * 
	 */
	@Autowired
	OrgMasterRepo orgMasterRepo;
	
	/**
	 * Invoice offer repo. This repository used to work with XpdInvoiceOffer entity. This entity contains data related to offers raised
	 * 						on invoices by supplier.
	 * 
	 */
	@Autowired
	InvoiceOfferRepo invoiceOfferRepo;
	
	/**
	 * Xpd Offer Reposiroty. This repository used to work with XpdOffer entity. This entity contains data related to offers raised
	 * 							by any particular company. 
	 */
	@Autowired
	XpdOfferRepo xpdOfferRepo;
	
	/**
	 * Invoice status reposiroty. This reposiroty used to work with XpdInvoiceStatus entity. This entity contains the status of any invoice. 
	 * 							
	 */
	@Autowired
	InvoiceStatusRepo invoiceStatusRepo;
	
	/**
	 * Date Repository. This repository is used to work with XpdNewPaymentDates entity. This entity provides new payment date as per 
	 * 						current date
	 */
	@Autowired
	DateRepo dateRepo;
	
	
	/**
	 * Offer criteria repository. This reposiroty used to work with XpdOfferCriteria. This entity contains information in case
	 * 							any specific offer is available from Buyer side
	 */
	@Autowired
	XpdOfferCriteriaRepo offerCriteriaRepo;
	
	
	@Autowired
	XpdCompanyOfferRepo companyOfferRepo;
	
	/**
	 * Invoice Notes repository. This repository is used to work with Invoices Notes entity. This entity holds notes data for invoices. 
	 * 								Those notes data can be Debit/Credit notes. 
	 */
	@Autowired
	InvoiceNotesRepo invoiceNotesRepo;
	
	@Autowired
	UserController userController;
	
	/**
	 * CustomConfigurationProperties are those properties which can be specific to the build/customer.
	 * 
	 */
	@Autowired
	CustomConfigurationProperties poolConfig;
	
	@Autowired
	XpdCompanyRelationshipRepo xpdCompanyRelationshipRepo;
	
	@Autowired
	PaymentStrategyRepo paymentStrategyRepo;
	
	@Autowired
	HolidaysRepo holidaysRepo;
	
	//This list holds all invoice notes which are used to filter notes as per selected invoice
	List<XpdInvoiceNoteLight> lstAllInvoicesNotes = null;
	
	//This list holds active invoice statuses. Which is used to filter invoice status as per selected invoice
	List<XpdInvoiceStatus> activeInvoiceStatuses = null;
	
	//This list holds active invoice offers. Which is used to filter invoice offers as per selected invoice
	List<XpdInvoiceOffer> activeInvoiceOffers = null;
	
	
	@Override
	public List<XpdOrgMaster> getCompanyLocations(@RequestParam String username) {
		XpdUser userObject = userRepo.findByUsername(username);
		XpdCompany currentCompany = xpdCompanyRepo.findOne(userController.getCurrentCompany(username).getId());
		if(null != userObject) {
			return new ArrayList<XpdOrgMaster>(currentCompany.getOrgMasters());
		} else 
			return null;
	}
	
	@Override
	public List<XpdOrgMaster> getBuyerLocations(@RequestParam String username) {
		XpdUser userObject = userRepo.findByUsername(username);
		if(null != userObject) {
			return orgMasterRepo.findAll(invoiceRepo.getAvailableInvoiceLocations(userController.getCurrentCompany(username).getId()));
		} else 
			return null;
	}
	
	@Override 
	public List<InvoiceDto> getLocationOpenInvoices(long currentLocationId, long buyerCompanyId, String username) {
		List<XpdInvoiceLight> allInvoices = null;
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), buyerCompanyId).getNextPaymentDate();
//		Date newPaymentDate = this.getNewPaymentDate(buyerCompanyId);
		final List<Long> currentLocationIds = new ArrayList<Long>();
		if (currentLocationId == 0) {
			XpdCompany buyerCompany = xpdCompanyRepo.findOne(buyerCompanyId);
			buyerCompany.getOrgMasters().forEach(buyer -> {
				currentLocationIds.add(buyer.getId());
			});
		} else  
			currentLocationIds.add(currentLocationId);
		allInvoices = invoiceRepo.getAllCompanyInvoicesWithDateFilter(currentLocationIds,userController.getCurrentCompany(username).getId(), newPaymentDate);
		List<XpdInvoiceLight> rejectedInvoices = invoiceRepo.getAllCompanyRejectedInvoicesWithDateFilter(currentLocationIds,userController.getCurrentCompany(username).getId(), newPaymentDate);
		for (XpdInvoiceLight xpdInvoice : allInvoices) {
			InvoiceDto currentDto = new InvoiceDto();
			currentDto.setXpdInvoice(xpdInvoice);
			currentDto.setActualAmountDue(this.getExactDueAmount(xpdInvoice.getId(), xpdInvoice.getTotalTrasactionValue()));
			currentDto.setActualAmountDue(xpdInvoice.getTotalTrasactionValue());
			currentDto.setNewPaymentDate(newPaymentDate);
			currentDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
			resultInvoices.add(currentDto);
		}
		for (XpdInvoiceLight xpdInvoice : rejectedInvoices) {
			InvoiceDto currentDto = new InvoiceDto();
			currentDto.setXpdInvoice(xpdInvoice);
			currentDto.setActualAmountDue(this.getExactDueAmount(xpdInvoice.getId(), xpdInvoice.getTotalTrasactionValue()));
//			currentDto.setActualAmountDue(xpdInvoice.getTotalTrasactionValue());
			currentDto.setNewPaymentDate(newPaymentDate);
			currentDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
			resultInvoices.add(currentDto);
		}
		return resultInvoices;
	}
	
	@Override 
	public List<InvoiceDto> getAllCompanyOpenInvoices(String username) throws ParseException {
//		logger.info("getAllCompanyOpenInvoices() : Getting all company open invoices for supplier");
//		XpdUser userObject = userRepo.findByUsername(username);
//		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
//		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate()).getNextPaymentDate();
//		List<XpdInvoiceLight> allInvoices = invoiceRepo.getAllCompanyInvoicesWithDateFilter(userController.getCurrentCompany(username).getId(), newPaymentDate);
//		List<XpdInvoiceLight> rejectedInvoices = invoiceRepo.getAllCompanyRejectedInvoicesWithDateFilter(userController.getCurrentCompany(username).getId(), newPaymentDate);
//		allInvoices.forEach(xpdInvoice -> {
//			InvoiceDto currentDto = new InvoiceDto();
//			currentDto.setXpdInvoice(xpdInvoice);
//			currentDto.setActualAmountDue(this.getExactDueAmount(xpdInvoice.getId(), xpdInvoice.getTotalTrasactionValue()));
//			currentDto.setNewPaymentDate(newPaymentDate);
//			currentDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
//			resultInvoices.add(currentDto);
//		});
//		rejectedInvoices.forEach(xpdInvoice -> {
//			InvoiceDto currentDto = new InvoiceDto();
//			currentDto.setXpdInvoice(xpdInvoice);
//			currentDto.setActualAmountDue(this.getExactDueAmount(xpdInvoice.getId(), xpdInvoice.getTotalTrasactionValue()));
//			currentDto.setNewPaymentDate(newPaymentDate);
//			currentDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
//			resultInvoices.add(currentDto);
//		});
//		logger.info("getAllCompanyOpenInvoices() : Got "+resultInvoices.size() + " Invoices.");
		return null;
	}
	
	@Override 
	public List<InvoiceDto> getLocationUnderClearingInvoices(long buyerId, long currentLocationId, String username) {
		logger.info("getLocationUnderClearingInvoices() : Getting all company under clearing invoices for supplier");
		List<XpdInvoiceLight> allInvoices = null;
		XpdUser userObject = userRepo.findByUsername(username);
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
		List<Long> locationIds = new ArrayList<Long>();
		if (currentLocationId == 0) {
			XpdCompany buyerCompany = xpdCompanyRepo.findOne(buyerId);
			buyerCompany.getOrgMasters().forEach(buyer -> {
				locationIds.add(buyer.getId());
			});
		} else  
			locationIds.add(currentLocationId);
		
		allInvoices = invoiceRepo.getSupplierUnderProcessingInvoices(locationIds, userController.getCurrentCompany(username).getId());
		
		for (XpdInvoiceLight xpdInvoice : allInvoices) {
			InvoiceDto clearingDto = new InvoiceDto();
			clearingDto.setXpdInvoice(xpdInvoice);
			clearingDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
			resultInvoices.add(clearingDto);
		}
		logger.info("getLocationUnderClearingInvoices() : Found "+resultInvoices.size() + " Invoices");
		return resultInvoices;
	}
	
	@Override 
	public SupplierDashboardDto getSupplierDashboardData(@RequestParam String username, long buyerId) {
		logger.info("getSupplierDashboardData() : Fetching Supplier dashboard data");
		XpdUser userObject = userRepo.findByUsername(username);
		SupplierDashboardDto supplierDashboardDto = new SupplierDashboardDto();
		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), buyerId).getNextPaymentDate();
//		Date newPaymentDate = this.getNewPaymentDate(buyerId);
		
//		My Work
		Double consolidateTotalAmountForUnderOfferInvoices = new Double(0);
		Long consolidateTotalInvoicesUnderOfferInvoices = new Long(0);
		Double openInvoicesCreditNotesValue = null;
		Double openInvoicesDebitNotesValue =null;
		
//		Getting all buyer companies for this supplier
		List<XpdCompany> buyerCompanies = getBuyerCompanies(username);
//		Getting locations for all Buyers
//		buyerCompanies.forEach(buyer-> {
//			Set<XpdOrgMaster> companyLocations = buyer.getOrgMasters();
//			List<Long> companyLocationIds = new ArrayList<Long>();
//			companyLocations.forEach(location -> {
//				companyLocationIds.add(location.getId());
//			});
//			
//			Date buyerPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), buyer.getId()).getNextPaymentDate();
//			if(companyLocationIds.size() > 0) {
//				Object totalInvoicesData=invoiceRepo.getSupplierDashboardDataWithBuyerLocations(userController.getCurrentCompany(username).getId(), buyerPaymentDate, companyLocationIds);
//				consolidateTotalAmountForUnderOfferInvoices += (((Object[])totalInvoicesData)[0]!=null)?(Double)((Object[])totalInvoicesData)[0]:0;
//				consolidateTotalInvoicesUnderOfferInvoices += (((Object[])totalInvoicesData)[1]!=null)?(Long)((Object[])totalInvoicesData)[1]:0;
//			}
//		});
		for(XpdCompany buyer: buyerCompanies ) {
			Set<XpdOrgMaster> companyLocations = buyer.getOrgMasters();
			List<Long> companyLocationIds = new ArrayList<Long>();
			companyLocations.forEach(location -> {
				companyLocationIds.add(location.getId());
			});
			Date buyerPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), buyer.getId()).getNextPaymentDate();
			if(companyLocationIds.size() > 0) {
				Object totalInvoicesData=invoiceRepo.getSupplierDashboardDataWithBuyerLocations(userController.getCurrentCompany(username).getId(), buyerPaymentDate, companyLocationIds);
				consolidateTotalAmountForUnderOfferInvoices += (((Object[])totalInvoicesData)[0]!=null)?(Double)((Object[])totalInvoicesData)[0]:0;
				consolidateTotalInvoicesUnderOfferInvoices += (((Object[])totalInvoicesData)[1]!=null)?(Long)((Object[])totalInvoicesData)[1]:0;
				
				openInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueSupplierInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), buyerPaymentDate, userController.getCurrentCompany(username).getId(), companyLocationIds);
				openInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueSupplierInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), buyerPaymentDate, userController.getCurrentCompany(username).getId(), companyLocationIds);
//				Handling the Credit and Debit notes
				if(openInvoicesCreditNotesValue != null) {
					consolidateTotalAmountForUnderOfferInvoices -= openInvoicesCreditNotesValue;
				}
				if(openInvoicesDebitNotesValue !=null) {
					consolidateTotalAmountForUnderOfferInvoices += openInvoicesDebitNotesValue;
				}
			}
			
		}
		
//		Object totalInvoicesData = invoiceRepo.getSupplierUnderOfferDashboardData(userController.getCurrentCompany(username).getId(), newPaymentDate);
//		Double totalAmountForUnderOfferInvoices = (((Object[])totalInvoicesData)[0]!=null)?(Double)((Object[])totalInvoicesData)[0]:0;
//		Long totalInvoicesForUnderOfferInvoices = (((Object[])totalInvoicesData)[1]!=null)?(Long)((Object[])totalInvoicesData)[1]:0;
//	
//		Double openInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueSupplierInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), newPaymentDate, userController.getCurrentCompany(username).getId());
//		Double openInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueSupplierInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), newPaymentDate, userController.getCurrentCompany(username).getId());
		
//		if (openInvoicesCreditNotesValue != null)
//			totalAmountForUnderOfferInvoices -= openInvoicesCreditNotesValue;
//		if (openInvoicesDebitNotesValue != null)
//			totalAmountForUnderOfferInvoices += openInvoicesDebitNotesValue;

//		supplierDashboardDto.setTotalAmount(totalAmountForUnderOfferInvoices);
		supplierDashboardDto.setTotalAmount(consolidateTotalAmountForUnderOfferInvoices);
//		supplierDashboardDto.setTotalInvoices(totalInvoicesForUnderOfferInvoices.intValue());
		supplierDashboardDto.setTotalInvoices(consolidateTotalInvoicesUnderOfferInvoices.intValue());
		
		Object underProcessingData = invoiceRepo.getSupplierUnderProcessingDashboardData(userController.getCurrentCompany(username).getId());
		Double totalAmountForUnderClearingInvoices = (((Object[])underProcessingData)[0]!=null)?(Double)((Object[])underProcessingData)[0]:0;
		Long totalInvoicesForUnderClearingInvoices = (((Object[])underProcessingData)[1]!=null)?(Long)((Object[])underProcessingData)[1]:0;

		Double underProcessingInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderApprovalSupplierInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), userController.getCurrentCompany(username).getId());
		Double underProcessingInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderApprovalSupplierInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), userController.getCurrentCompany(username).getId());
		
		if (underProcessingInvoicesCreditNotesValue != null)
			totalAmountForUnderClearingInvoices -= underProcessingInvoicesCreditNotesValue;
		if (underProcessingInvoicesDebitNotesValue != null)
			totalAmountForUnderClearingInvoices += underProcessingInvoicesDebitNotesValue;
		
		supplierDashboardDto.setAmountUnderClearing(totalAmountForUnderClearingInvoices);
		supplierDashboardDto.setNumberOfInvoicesUnderClearning(totalInvoicesForUnderClearingInvoices.intValue());

		Object completedData = invoiceRepo.getSupplierCompletedDashboardData(userController.getCurrentCompany(username).getId());
//		Setting amountDiscountedTillDate to Zero if no invoices are Paid
		if ( ( ((Object[])completedData)[0] ) == null ) {
			supplierDashboardDto.setAmountDiscountedTillDate(0);
		} else {
			supplierDashboardDto.setAmountDiscountedTillDate((Double)((Object[])completedData)[0]);
		}

//		Setting daysRecievedEarly to Zero if no invoices are Paid
		if ( ( ((Object[])completedData)[1] ) == null ) {
			supplierDashboardDto.setDaysReceivedEarly(0);
		} else {
			supplierDashboardDto.setDaysReceivedEarly(((Double)((Object[])completedData)[1]).intValue());
		}
		supplierDashboardDto.setCurrencySymbol(getCompanyTransactionCurrency(userController.getCurrentCompany(username).getId()).getSymbol());
		logger.info("getSupplierDashboardData() : Fetching Supplier dashboard data successfully");
		return supplierDashboardDto;
	}
	
	private void getOrgMasters() {
	// TODO Auto-generated method stub
	
}
@Override
	synchronized public boolean submitInvoiceOffer(@RequestBody InvoiceOfferDto invoiceOfferDto) {
		logger.info("submitInvoiceOffer() : Submitting invoice offer for " + invoiceOfferDto.getLstInvoices().size() + " Invoices at " + invoiceOfferDto.getSelectedPrice().toString() + " Percentage");
		BigDecimal percentage = invoiceOfferDto.getSelectedPrice();
		double annualPercentage = percentage.doubleValue() * 365/30;
		XpdUser userObject = userRepo.findByUsername(invoiceOfferDto.getUsername());
		XpdCompany currentCompany = userController.getCurrentCompany(invoiceOfferDto.getUsername());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		Making HTML for email
		
		double sumOfInvoices = 0;
		StringBuffer emailContent = new StringBuffer();
		// Xpd Offer creation
		SysOfferType offerType = xpdOfferRepo.getSupplierOfferType();
		XpdOffer xpdOffer = new XpdOffer();
		xpdOffer.setOfferName(percentage.toString()+" offer");
		xpdOffer.setSysOfferTypeId(offerType);
		xpdOfferRepo.save(xpdOffer);
		
		List<XpdInvoiceOffer> invoiceOffersToSave = new ArrayList<XpdInvoiceOffer>();
		List<XpdInvoiceStatus> invoiceStatusToSave = new ArrayList<XpdInvoiceStatus>();
		List<List<Long>> invoiceOffersToUpdate = new ArrayList<List<Long>>();
		List<List<Long>> invoiceStatusToUpdate = new ArrayList<List<Long>>();
		XpdStatus firstWorkflowStatus = invoiceStatusRepo.getWorkflowStatusBySequenceId(2);
		invoiceStatusToUpdate.add(new ArrayList<>());
		invoiceOffersToUpdate.add(new ArrayList<>());
		int currentIndex = 0;
		
		for (XpdInvoice xpdInvoice : invoiceOfferDto.getLstInvoices()) {
			sumOfInvoices = sumOfInvoices + xpdInvoice.getTotalTrasactionValue();
			XpdInvoiceOffer invoiceOffer = new XpdInvoiceOffer();
			double totalPaymentDue = this.getExactDueAmount(xpdInvoice.getId(), xpdInvoice.getTotalTrasactionValue());
			
			long daysBetween = InvoiceUtil.getDaysBetween(invoiceOfferDto.getNextDueDate(), xpdInvoice.getDueDate());
			
			invoiceOffer.setAnnualPercentage(annualPercentage);
			double targetAveragePercentage = percentage.doubleValue()/30*daysBetween;
			double discountAmount = totalPaymentDue * targetAveragePercentage/100;
			double netAmount = totalPaymentDue - discountAmount;
			
			invoiceOffer.setDiscountAmount(discountAmount);
			invoiceOffer.setAppliedDiscount(targetAveragePercentage);
			invoiceOffer.setDiscountPercent30Days(percentage.doubleValue());
			invoiceOffer.setNetAmount(netAmount);
			invoiceOffer.setDaysRemaining(daysBetween);
			invoiceOffer.setXpdInvoiceId(xpdInvoice);
			invoiceOffer.setXpdCompanyId(currentCompany);
			invoiceOffer.setXpdOfferId(xpdOffer);
			invoiceOffer.setIsActive(true);
			invoiceOffer.setCreatedBy(userObject.getId());
			invoiceOffer.setCreatedDate(new Date());
			invoiceOffer.setNewDueDate(invoiceOfferDto.getNextDueDate());
			invoiceOffer.setDueAmount(totalPaymentDue);
			invoiceOffer.setXpdOrgMasterId(xpdInvoice.getXpdOrgMasterId());
			invoiceOffersToSave.add(invoiceOffer);

			XpdInvoiceStatus xpdInvoiceStatus = new XpdInvoiceStatus();
			xpdInvoiceStatus.setInvoiceId(xpdInvoice);
			xpdInvoiceStatus.setStatusId(firstWorkflowStatus);
			xpdInvoiceStatus.setIsCurrent(1);
			xpdInvoiceStatus.setCreatedDate(new Date());
			
			xpdInvoiceStatus.setCreatedBy(userObject);
			invoiceStatusToSave.add(xpdInvoiceStatus);
			
			if(invoiceStatusToUpdate.get(currentIndex).size() > 29999) {
				currentIndex++;
				invoiceStatusToUpdate.add(currentIndex, new ArrayList<>());
				invoiceOffersToUpdate.add(currentIndex, new ArrayList<>());
			}
			invoiceOffersToUpdate.get(currentIndex).add(xpdInvoice.getId());
			invoiceStatusToUpdate.get(currentIndex).add(xpdInvoice.getId());

			emailContent.append("<tr>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ xpdInvoice.getInvoiceNumber()+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(totalPaymentDue)+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(discountAmount)+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(netAmount)+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(xpdInvoice.getDueDate() )+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoiceOfferDto.getNextDueDate())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'><font color='blue'>Submitted For Approval</font></td>"
	        		+ "</tr>");
		}
		for(int count=0; count <=currentIndex;count++) {
			invoiceStatusRepo.inactiveCurrentInvoiceStatus(invoiceStatusToUpdate.get(count));
			invoiceOfferRepo.inactiveCurrentInvoiceOffer(invoiceOffersToUpdate.get(count));
		}
		invoiceOfferRepo.save(invoiceOffersToSave);
		invoiceStatusRepo.save(invoiceStatusToSave);
//		Notification section starts ( SMS and EMAIL )
		emailContent.append("</table>"
		+ "<br><br>"
		+ "This request is currently under review with the Buyer. We will update you with the final status once confirmed at our end."
		+ "<br><br>"
		+ "To view the status of your request, please login at https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com"
		+ "<br><br>"
		+ "For any assistance, you can directly reach out to us on info@xpedize.com or call on +91 9810325445 / +91 9820670506."
		+ "<br><br>");

		emailContent.insert(0,"Dear "+ currentCompany.getCompanyName() +",<br><br>"
				+ "You have successfully submitted the request for early payment on the following invoices.<br><br>"
				+ "<b>Total Invoice Amount:</b> INR "+Math.round(sumOfInvoices)+"<br>" // Using sum of total_transaction_value here
				+ "<b>No of Invoices:</b> "+invoiceOfferDto.getLstInvoices().size()+"<br>"
				+ "<b>Submission Date:</b> "+sdf.format(new Date())+"<br><br>"
				+"<table style='border: 1px solid black;border-collapse: collapse;text-align: center;display: block;width: auto; max-height: 200px;overflow-y: auto; border-style: none;'>"
				+ "<tr style='background-color: lightgrey'>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Invoice Number</th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Amount Due<div><small>(INR)</small></div></th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Discount Amount<div><small>(INR)</small></div></th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Net Amount<div><small>(INR)</small></div></th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Original Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>New Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Status</th>"
				+ "</tr>");
		
		SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
		emailJob.setMessage(emailContent.toString());
		emailJob.setToAddress(currentCompany.getEmail());
		emailJob.setSubject("Success! Early Payment Request Submitted");
		try {
			Thread emailThread = new Thread(emailJob);
			emailThread.start();
		} catch(Exception ee) {
			System.out.println("Error in sending Mail....!");
		}
		
//		SMS
		SendSMSJob smsJob = new SendSMSJob();
//		TextLocalSMSJob smsJob = new TextLocalSMSJob();
		try {
			int noOfInvoices = invoiceOfferDto.getLstInvoices().size();
			String dateOfSub = sdf.format(new Date());
			String loginLink = "https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com";
			sumOfInvoices = Math.round(sumOfInvoices);
			
			System.out.println(dateOfSub +"|"+loginLink+"|"+ sumOfInvoices);
			smsJob.setMessage("Success ! You have submitted a request for early payment on "
					+noOfInvoices+" Invoices amounting to INR "
					+sumOfInvoices+" on "+dateOfSub+"."
					+loginLink
					);
//			Use below line if using SendSMSJob Class for SMS
			smsJob.setTopicARN(userObject.getArn().toString());
			
//			Use below line if using TextLocalSMSJob class for SMS
//			smsJob.setMobileNumber(userObject.getMobileNumber().toString());
			
			Thread job = new Thread(smsJob);
			job.start();
			
		} catch(Exception e) {
			System.out.println(e);
		}
		logger.info("submitInvoiceOffer() : Invoice offers submitted successfully for " + invoiceOfferDto.getLstInvoices().size() + " Invoices at " + invoiceOfferDto.getSelectedPrice().toString() + " Percentage");
		return true;
	}

	@Override
	public XpdOfferDto getCompanyInvoiceOffer(String username, long buyerId) throws ParseException {
		logger.info("getCompanyInvoiceOffer() : Fetching company specific invoice offer for buyer ID "+ buyerId);
		XpdUser userObject = userRepo.findByUsername(username);
//		List<XpdCompanyOffer> companyOffers = 
		XpdCompany currentCompany = userController.getCurrentCompany(username);
		XpdCompanyOffer companyOffer = companyOfferRepo.findBuyerCompanyOffer(currentCompany.getId(), buyerId);
		XpdOfferDto offerDto = new XpdOfferDto();
		offerDto.setTargetAveragePercentage(new BigDecimal(2.0));
		Date currentDate = new Date();
		DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
		if(companyOffer != null) {
			if ((companyOffer.getStartDate() == null || dateTimeComparator.compare(companyOffer.getStartDate(), currentDate) <= 0) && 
				(companyOffer.getEndDate() == null || dateTimeComparator.compare(companyOffer.getEndDate(),currentDate) >= 0 ))
			{
				logger.info("getCompanyInvoiceOffer() : Specific invoice offer found for buyer ID "+ buyerId);
				List<XpdOfferCriteria> offerCriterias = companyOffer.getXpdOffer().getOfferCriterias();
				if (offerCriterias != null && !offerCriterias.isEmpty() && offerCriterias.size() > 0) {
					XpdOfferCriteria offerCriteria = offerCriterias.get(0);
					XpdOffer offer = offerCriteria.getXpdOffer();
					offerDto.setId(offer.getId());
					offerDto.setTargetAveragePercentage(BigDecimal.valueOf(Double.parseDouble(offerCriteria.getValue())));
					if (offerCriteria.getSysOfferParameterId().getParameter().equals(AppConstants.BUYER_FIXED_OFFER_TYPE)) {
						offerDto.setTargetAveragePercentage(BigDecimal.valueOf(Double.parseDouble(offerCriteria.getValue())));
						offerDto.setFixedOffer(true);
					}
					else if (offerCriteria.getSysOfferParameterId().getParameter().equals(AppConstants.BUYER_MINIMUM_OFFER_TYPE)) {
						BigDecimal minPercentage = BigDecimal.valueOf(0);
						Double defaultPercentage = new Double(currentCompany.getDefaultPercentage());
						minPercentage = BigDecimal.valueOf(Double.parseDouble(offerCriteria.getValue()));
						offerDto.setMinPercentage(minPercentage);
						if (minPercentage.doubleValue() <= defaultPercentage) {
							offerDto.setTargetAveragePercentage(BigDecimal.valueOf(2));
						} else {
							offerDto.setTargetAveragePercentage(minPercentage);
						}
						
						offerDto.setMaxPercentage(offerCriteria.getMaxPercentage());
					}
				}
			} else
				logger.info("Given offer is not valid, Sending fresh offer for buyer ID " + buyerId);
		}
		else {
			offerDto.setTargetAveragePercentage(new BigDecimal(currentCompany.getDefaultPercentage()));
			logger.info("getCompanyInvoiceOffer() : Sending fresh invoice offer for buyer ID "+ buyerId);
		}
		return offerDto;
	}

	@Override
	public List<InvoiceDto> getApprovedInvoices(String username, 
			String buyerId,
			String location,
			String fromDate,
			String toDate,
			String invoiceNumber) throws ParseException {
		logger.info("getApprovedInvoices() : Fetching Paid invoices for supplier");
		List<XpdInvoiceLight> allInvoices = null;
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
		XpdUser userObject = userRepo.findByUsername(username);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		fromDate = (fromDate.isEmpty())?fromDate:fromDate+" 00:00:01";
		toDate = (toDate.isEmpty())?toDate:toDate+" 23:59:59";
		Date fDate = (fromDate.isEmpty())?null:dateFormat.parse(fromDate);
		Date tDate = (toDate.isEmpty())?null:dateFormat.parse(toDate);
		
		XpdCompany currentCompany = userController.getCurrentCompany(username);
		List<Long> locationIds = new ArrayList<Long>();
		if ((Integer.parseInt(location)) == 0) {
			XpdCompany buyerCompany = xpdCompanyRepo.findOne((long) (Integer.parseInt(buyerId)));
			buyerCompany.getOrgMasters().forEach(buyer -> {
				locationIds.add(buyer.getId());
			});
		} else  
			locationIds.add((long) (Integer.parseInt(location)));
		
		if (invoiceNumber.equals(""))
			invoiceNumber="%";
		else
			invoiceNumber = "%"+invoiceNumber+"%";
		
//		if (location.equals("All"))
//			location = "%";
		
		if(fDate != null && tDate != null)
			allInvoices = invoiceRepo.getSupplierPaidInvoices(currentCompany.getId(),
					locationIds, fDate, tDate,invoiceNumber);
		else if (fDate==null && tDate != null)
			allInvoices = invoiceRepo.getSupplierPaidInvoicesWithoutFromDate(currentCompany.getId(),
					locationIds, tDate, invoiceNumber);
		else if (fDate != null && tDate==null) 
			allInvoices = invoiceRepo.getSupplierPaidInvoicesWithoutToDate(currentCompany.getId(),
					locationIds, fDate, invoiceNumber);
		else 
			allInvoices = invoiceRepo.getSupplierPaidInvoicesWithoutStartEndDates(currentCompany.getId(), locationIds, invoiceNumber);
		
		for (XpdInvoiceLight xpdInvoice : allInvoices) {
			InvoiceDto clearingDto = new InvoiceDto();
			clearingDto.setXpdInvoice(xpdInvoice);
			clearingDto.setNotesSummary(this.getInvoiceNotes(xpdInvoice.getId(), xpdInvoice.getCurrency()));
			resultInvoices.add(clearingDto);
		}
		logger.info("getApprovedInvoices() : "+resultInvoices.size() + " Invoices found");
		return resultInvoices;
	}
	
	private CurrencyDto getCompanyTransactionCurrency(long companyId) {
		List<CurrencyDto> currencyDtos = xpdCompanyRepo.getSupplierRelationships(companyId);
		if(currencyDtos != null && currencyDtos.size() > 0) {
			return currencyDtos.get(0);
		}
		return null;
	}

	@Override
	public boolean generateOTP(String username) {
		XpdUser userObject = userRepo.findByUsername(username);
		Random rnd = new Random();
		int randomNumber = 100000 + rnd.nextInt(900000);
		System.out.println(randomNumber);
		try {
//			SendSMSJob smsJob = new SendSMSJob();
//			try {
//				smsJob.setMessage(""
//						+ "The generated OTP is  " + randomNumber +". Please do not share one time password with anyone\n"
//								+ "This OTP is valid for 5 minutes."
//						+ "\n"
//						+ "Thanks,\n"
//						+ "Team Xpedize");
//				smsJob.setTopicARN(userObject.getArn().toString());
//			} catch(Exception e) {
//				System.out.println("Error: "+e);
//			}
//			Thread job = new Thread(smsJob);
//			job.start();
			
			TextLocalSMSJob smsJob = new TextLocalSMSJob();
			smsJob.setMessage("The generated OTP is "+randomNumber+". Please do not share one time password with anyone." + 
					"This OTP is valid for 5 minutes." + 
					"Thanks," + 
					"Team Xpedize");
			
			try {
				smsJob.setMobileNumber(userObject.getMobileNumber().toString());
				Thread smsThread = new Thread(smsJob);
				smsThread.start();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			OTPDetails otpDetails = new OTPDetails(randomNumber, new Date());
			SessionUtil.getInstance().userOTPMap.put(username, otpDetails);
			String emailContent = "Hi " + userController.getCurrentCompany(username).getCompanyName() + ", \n\n"
					+ "The generated OTP is  " + randomNumber +". Please do not share one time password with anyone.\n"
							+ "This OTP is valid for 5 minutes.\n"
							+ "\n"
							+ "Thanks,\n"
							+ "Team Xpedize";
			SendEmailJob emailJob = new SendEmailJob();
			emailJob.setEmailContent(emailContent);
			emailJob.setEmailId(userController.getCurrentCompany(username).getEmail());
			emailJob.setEmailSubject("OTP Generated Successfully");
			Thread emailThread = new Thread(emailJob);
			emailThread.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private double getExactDueAmount(long invoiceId, double totalPaymentDue) {
		if (this.lstAllInvoicesNotes == null) {
			this.lstAllInvoicesNotes = invoiceNotesRepo.findAllNotes("XPD%");
		}
		List<XpdInvoiceNoteLight> invoiceNotes = lstAllInvoicesNotes.stream().filter(note -> note.getInvoiceId()==invoiceId).collect(Collectors.toList());
		
		for (XpdInvoiceNoteLight xpdInvoiceNote : invoiceNotes) {
			if (xpdInvoiceNote.getNoteDesc().equals(NoteTypeEnum.CREDIT_MEMO.getValue())) {
				totalPaymentDue -= xpdInvoiceNote.getTotalTrasactionValue();
			} else if (xpdInvoiceNote.getNoteDesc().equals(NoteTypeEnum.DEBIT_MEMO.getValue())) {
				totalPaymentDue += xpdInvoiceNote.getTotalTrasactionValue();
			}
		}
		return totalPaymentDue;
	}
	

	private String getInvoiceNotes(final long invoiceId, String currency) {
		if (this.lstAllInvoicesNotes == null) {
			this.lstAllInvoicesNotes = invoiceNotesRepo.findAllNotes("XPD%");
		}
		List<XpdInvoiceNoteLight> invoiceNotes = this.lstAllInvoicesNotes.stream().filter(note -> note.getInvoiceId()==invoiceId).collect(Collectors.toList());
		StringBuffer notesBuffer = new StringBuffer("");
		for (XpdInvoiceNoteLight note : invoiceNotes) {
			SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
			String dd = fd.format(note.getCreatedDate());
			notesBuffer.append(note.getNoteDesc()+" | "+currency+" " + note.getTotalTrasactionValue() +" | "+dd+ "\n");
//			System.out.println("Date: "+dd+" | Type: "+note.getNoteDesc() + " | Amount: "+currency+" " + note.getTotalTrasactionValue() + "\n");
		}
		return notesBuffer.toString();
	}
	
	@Override
	public boolean checkOTPIsValid(String username, String otp) {
		OTPDetails savedOTP = SessionUtil.getInstance().userOTPMap.get(username);
		Long generatedTime = savedOTP.getTimeOfGeneration().getTime();
		Long currentTime = new Date().getTime();
		long lapsedTime = currentTime - generatedTime;
		long seconds = lapsedTime/1000;
		if (seconds < poolConfig.getOtpTimeout()) {
			boolean isOtpCorrect = (savedOTP.getOtp() == Integer.parseInt(otp));
			if (isOtpCorrect) {
				SessionUtil.getInstance().userOTPMap.remove(username);
				return true;
			} else
				return false;
		} else {
			SessionUtil.getInstance().userOTPMap.remove(username);
			return false;
		}
	}
	
	@Override
	public boolean getAuth(String username) {
		XpdUser userObj = userRepo.findByUsername(username);
		if (userObj != null) {
			return userObj.isTNC();
		} else {
			return false;
		}
	}
	
	public boolean checkPAN(String username, String nPAN) {
		XpdUser userObj = userRepo.findByUsername(username);
		String temp = userController.getCurrentCompany(username).getPan();
		if (temp.equals("")) {
			System.out.println("No PAN found for "+userController.getCurrentCompany(username).getCompanyName());
			return false;
		} else {
			if(temp.equals(nPAN)) {
				return true;
			} else
				return false;
		}
	}
	
	@Override
	public boolean saveMobileNumber(String username, String Mob) {
		XpdUser userObj = userRepo.findByUsername(username);
		Mob = "+91"+Mob;
		if(userObj != null) {
			if (username.length() >= 13) {
				username=username.substring(0, 12);
			} else {
				username = username.substring(0, username.length()-1);
			}
				username = username.replaceAll("[-+.^:,@_!#$%&'*/=?`{|}~]", "");
				System.out.println("Removed special characters from username: "+username);
				String ACCESS_KEY = "AKIAJ2PJD6ZAU5O45OAA";
	        	String SECRET_KEY = "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";
	        	AmazonSNSClient snsClient = new AmazonSNSClient(new  BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
	        	snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));        
	            CreateTopicRequest createTopicRequest = new CreateTopicRequest("XPD-"+username);
	            CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
	            SubscribeRequest subRequest = new SubscribeRequest(createTopicResult.getTopicArn(), "sms", Mob);
	            snsClient.subscribe(subRequest);
//	            return createTopicResult.getTopicArn();
			userObj.setMobileNumber(Mob);
			userObj.setTNC(true);
			userObj.setCreatedDate(new Date());
			userObj.setModifiedDate(new Date());
			userObj.setArn(createTopicResult.getTopicArn());
			userRepo.save(userObj);		
//			System.out.println("Mobile Number("+Mob+") stored and ARN( "+createTopicResult.getTopicArn()+" ) created for user: "+userObj.getUsername()+" of Company: "+userController.getCurrentCompany(username).getCompanyName());
			System.out.println("Storing Mobile Number "+Mob+" and ARN "+createTopicResult.getTopicArn()+" for User: "+userObj.getUsername());
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean checkUserRole(String username) {
		XpdUser userObj = userRepo.findByUsername(username);
		if(userObj != null) {
			return userObj.getIsBuyer();
		} else {
			return false;
		}
		
	}
	
	@Override
	public boolean sendPasswordChangeEmail(@RequestParam String username) {
		XpdUser user = userRepo.findByUsername(username);
		XpdCompany currentCompany = userController.getCurrentCompany(username);
		
		if(user != null) {
			StringBuffer emailContent = new StringBuffer();
			emailContent.append("Dear "+currentCompany.getCompanyName()+",<br><br>"
					+ "Your password for Xpedize has been changed successfully.<br><br>"
					+ "We have received a request to have your password reset for "+user.getUsername()+". "
					+ "If you did not make this request, please reach out to us immediately on info@xpedize.com"
					+ " or call on +91 9810325445 / +91 9820670506.<br><br>"
					+ "");
			
//		Sending Email and SMS from here
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
			
//			SMS
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
		} else {
			return false; 
		}
	}

	@Override
	public boolean companyIsBuyer(String username) {
//		XpdUser user = userRepo.findByUsername(username);
//		XpdCompany company= userController.getCurrentCompany(username);
		List<XpdCompanyRelationship> lstBuyerRelationshipList = xpdCompanyRepo.isBuyerCompany(userController.getCurrentCompany(username).getId());
		return (lstBuyerRelationshipList != null && lstBuyerRelationshipList.size()>0);
	}
	
//	Get Buyer Companies
	@Override
	public List<XpdCompany> getBuyerCompanies(String username) {
//		XpdUser user = userRepo.findByUsername(username);
		XpdCompany company= userController.getCurrentCompany(username);
		List<XpdCompany> buyerCompanies = xpdCompanyRelationshipRepo.getBuyersCompaniesForSupplier(company.getId());
		return buyerCompanies;
	}

	@Override
	public Date getNewPaymentDate(long companyId) {
		Calendar cal = Calendar.getInstance();
		XpdPaymentDateStrategy strategy = paymentStrategyRepo.findPaymentStrategyForCompany(companyId);
		List<XpdHoliday> companyHolidays = holidaysRepo.getHolidaysForCompany(companyId);
		
		if (strategy != null) {
			cal.add(Calendar.DAY_OF_MONTH, strategy.getMinimumDaysGap());
			if (strategy.getPaymentDateStrategy().equals(InvoiceUtil.PAYMENT_DATE_STRATEGY_WEEKDAY)) {
				while( strategy.getParticularDayOfWeek().indexOf(String.valueOf(cal.get( Calendar.DAY_OF_WEEK ))) == -1 
						|| isHoliday(cal.getTime(), companyHolidays)
						|| strategy.getWorkingDays().indexOf(String.valueOf(cal.get( Calendar.DAY_OF_WEEK ))) == -1)
				    cal.add( Calendar.DATE, 1 );  
			} else {
				while(isHoliday(cal.getTime(), companyHolidays) || strategy.getWorkingDays().indexOf(String.valueOf(cal.get( Calendar.DAY_OF_WEEK ))) == -1)
				    cal.add( Calendar.DATE, 1 );
			}
		} else {
			cal.add(Calendar.DAY_OF_MONTH, 2);
			while( cal.get( Calendar.DAY_OF_WEEK ) != 5 
					|| isHoliday(cal.getTime(), companyHolidays))
				cal.add( Calendar.DATE, 1 );
		}
		return cal.getTime();
	}
	
	private boolean isHoliday(Date givenDate, List<XpdHoliday> holidayList) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		boolean isHoliday = false;
		String dt = sdf.format(givenDate);
		for (XpdHoliday holiday : holidayList) {
			String holidayDate = sdf.format(holiday.getHolidayDate());
			if (holidayDate.equals(dt)) {
				isHoliday = true;
				break;
			}
		}
		return isHoliday;
	}
	
}
