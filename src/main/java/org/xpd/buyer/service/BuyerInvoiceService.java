package org.xpd.buyer.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.xpd.app.controller.UserController;
import org.xpd.models.SysOfferParameter;
import org.xpd.models.SysOfferType;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdCompanyOffer;
import org.xpd.models.XpdCompanyRelationship;
import org.xpd.models.XpdInvoice;
import org.xpd.models.XpdInvoiceOffer;
import org.xpd.models.XpdInvoiceStatus;
import org.xpd.models.XpdOffer;
import org.xpd.models.XpdOfferCriteria;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.XpdStatus;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.CurrencyDto;
import org.xpd.models.dto.XpdCompanyOfferDto;
import org.xpd.models.dto.XpdInvoiceLight;
import org.xpd.models.dto.XpdInvoiceNoteLight;
import org.xpd.models.dto.XpdInvoiceStatusLight;
import org.xpd.repo.DateRepo;
import org.xpd.repo.InvoiceNotesRepo;
import org.xpd.repo.InvoiceOfferRepo;
import org.xpd.repo.InvoiceRepo;
import org.xpd.repo.InvoiceStatusRepo;
import org.xpd.repo.OrgMasterRepo;
import org.xpd.repo.SysOfferParameterRepo;
import org.xpd.repo.UserRepo;
import org.xpd.repo.XpdCompanyOfferRepo;
import org.xpd.repo.XpdCompanyRelationshipRepo;
import org.xpd.repo.XpdCompanyRepo;
import org.xpd.repo.XpdOfferCriteriaRepo;
import org.xpd.repo.XpdOfferRepo;
import org.xpd.repo.XpdStatusRepo;
import org.xpd.supplier.dto.ApproveRejectRequestDto;
import org.xpd.supplier.dto.BuyerDashboardDto;
import org.xpd.supplier.dto.InvoiceDto;
import org.xpd.supplier.dto.InvoiceStatusEnum;
import org.xpd.supplier.dto.NoteTypeEnum;
import org.xpd.supplier.service.IInvoiceService;
import org.xpd.util.AppConstants;
import org.xpd.util.CustomConfigurationProperties;
import org.xpd.util.InvoiceUtil;
import org.xpd.util.SendInvoiceEmailJob;
import org.xpd.util.SendSMSJob;

/**
 * @author Pawan Juneja
 *	This service provides APIs which are related to all the screens of Buyer dashboard.
 * 
 * @implements IBuyerInvoiceService
 * 
 */
@Service
public class BuyerInvoiceService implements IBuyerInvoiceService {

	Logger logger = LoggerFactory.getLogger(BuyerInvoiceService.class);
	
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
	 * Xpd Status reposiroty. This repository is used to work with XpdStatus entity. This entity holds data of all different statuses 
	 * 							available for any invoice. 
	 */
	@Autowired
	XpdStatusRepo xpdStatusRepo;
	
	/**
	 * Invoice Notes repository. This repository is used to work with Invoices Notes entity. This entity holds notes data for invoices. 
	 * 								Those notes data can be Debit/Credit notes. 
	 */
	@Autowired
	InvoiceNotesRepo invoiceNotesRepo;
	
	/**
	 * Date Repository. This repository is used to work with XpdNewPaymentDates entity. This entity provides new payment date as per 
	 * 						current date
	 */
	@Autowired
	DateRepo dateRepo;
	
	@Autowired
	UserController userController;
	
	@Autowired 
	XpdCompanyOfferRepo companyOfferRepo;
	
	@Autowired
	XpdOfferCriteriaRepo criteriaRepo;
	
	@Autowired
	XpdCompanyRelationshipRepo companyRelationshipRepo;
	
	@Autowired
	CustomConfigurationProperties poolConfig;
	
	//This list holds all invoice notes which are used to filter notes as per selected invoice
	List<XpdInvoiceNoteLight> lstAllInvoicesNotes = null;
	
	//This list holds active invoice offers. Which is used to filter invoice offers as per selected invoice 
	List<XpdInvoiceOffer> activeInvoiceOffers = null;
	
	//This list holds active invoice statuses. Which is used to filter invoice status as per selected invoice
	List<XpdInvoiceStatusLight> activeInvoiceStatuses = null;

	@Autowired
	IInvoiceService invoiceService;
	
	@Autowired
	SysOfferParameterRepo offerParameterRepo;
	
	private StringBuffer emailContent = new StringBuffer();
	
	private int NOI=0;
	
	
	
	@Override
	public List<XpdOrgMaster> getBuyerCompanyLocations(@RequestParam String username) {
//		XpdUser userObject = userRepo.findByUsername(username);
//		if(null != userObject) {
//			invoiceRepo.getAvailableInvoiceLocations(userObject.getXpdcompany().getId());
//			return orgMasterRepo.findAll(invoiceRepo.getAvailableInvoiceLocations(userObject.getXpdcompany().getId()));
//		} else 
			return null;
	}
	
	@Override
	public List<SysOfferParameter> getAllOfferTypes() {
		return offerParameterRepo.findAll();
	}
	

	@Override 
	public BuyerDashboardDto getBuyerDashboardData(@RequestParam String username) throws Exception {
		BuyerDashboardDto buyerDashboardDto = new BuyerDashboardDto();
		XpdUser userObject = userRepo.findByUsername(username);
		CurrencyDto invoiceCurrency = getCompanyTransactionCurrency(getCurrentCompany(username).getId());
		Set<XpdOrgMaster> companyLocations = null;
		companyLocations = getCurrentCompany(username).getOrgMasters();
		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), getCurrentCompany(username).getId()).getNextPaymentDate();
//		Date newPaymentDate = invoiceService.getNewPaymentDate(getCurrentCompany(username).getId());
		if (companyLocationIds.size() > 0) {
			Object openInvoicesData = invoiceRepo.getBuyerUnderOfferDashboardData(companyLocationIds, newPaymentDate);
			Double openInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), newPaymentDate, companyLocationIds);
			Double openInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForPaymentsDueInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), newPaymentDate, companyLocationIds);
	
			Double openInvoicesTotalAmount = (Double)openInvoicesData;
			if (openInvoicesCreditNotesValue != null)
				openInvoicesTotalAmount -= openInvoicesCreditNotesValue;
			if (openInvoicesDebitNotesValue != null)
				openInvoicesTotalAmount += openInvoicesDebitNotesValue;
			buyerDashboardDto.setOpenInvoicesTotalAmount(openInvoicesTotalAmount);
			
			
			Object underApprovalData = invoiceRepo.getBuyerUnderApprovalDashboardData(companyLocationIds);
			Double underApprovalInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderApprovalInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), companyLocationIds);
			Double underApprovalInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderApprovalInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), companyLocationIds);
			Double underApprovalTotalAmount = (underApprovalData != null)?(Double)underApprovalData:0;
			
			
			if (underApprovalInvoicesCreditNotesValue != null)
				underApprovalTotalAmount -= underApprovalInvoicesCreditNotesValue;
			if (underApprovalInvoicesDebitNotesValue != null)
				underApprovalTotalAmount += underApprovalInvoicesDebitNotesValue;
			buyerDashboardDto.setApprovalPendingAmount(underApprovalTotalAmount);
			
			
	
			Object underClearingData = invoiceRepo.getBuyerUnderClearingDashboardData(companyLocationIds);
			Double underClearingInvoicesCreditNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderClearingInvoices("XPD%", NoteTypeEnum.CREDIT_MEMO.getValue(), companyLocationIds);
			Double underClearingInvoicesDebitNotesValue = invoiceNotesRepo.getSumOfNotesValueForUnderClearingInvoices("XPD%", NoteTypeEnum.DEBIT_MEMO.getValue(), companyLocationIds);
	
			Double underClearingTotalAmount = (underClearingData != null)?(Double)underClearingData:0;
	
			if (underClearingInvoicesCreditNotesValue != null)
				underClearingTotalAmount -= underClearingInvoicesCreditNotesValue;
			if (underClearingInvoicesDebitNotesValue != null)
				underClearingTotalAmount += underClearingInvoicesDebitNotesValue;
			
			buyerDashboardDto.setUnderClearingAmount(underClearingTotalAmount);
			
			Object paidInvoicesData = invoiceRepo.getBuyerPaidDashboardData(companyLocationIds);
			if (paidInvoicesData == null) {
				buyerDashboardDto.setTotalSavingsAchievedAmount((double) 0);
			} else {
				buyerDashboardDto.setTotalSavingsAchievedAmount((Double)paidInvoicesData);
			}
			
			Object annualPercentageAchievedData = invoiceRepo.getBuyerPaidAPRDashboardData(companyLocationIds);
			if (annualPercentageAchievedData == null) {
				buyerDashboardDto.setAprPercentage(0);
			} else {
				buyerDashboardDto.setAprPercentage((Double)annualPercentageAchievedData);
			}
			
			buyerDashboardDto.setTotalAmount(new Double(0));
			buyerDashboardDto.setTotalWeightedAmount(new Double(0));
			List<XpdInvoiceLight> locationInvoices = invoiceRepo.getBuyerUnderOfferDaysDashboardData(companyLocationIds, newPaymentDate);
			locationInvoices.forEach(invoice -> {
				long daysRemaining = InvoiceUtil.getDaysBetween(new Date(), invoice.getDueDate());
				double actualAmountDue = getExactDueAmount(invoice.getId(), invoice.getTotalTrasactionValue());
				buyerDashboardDto.setTotalAmount(buyerDashboardDto.getTotalAmount()+actualAmountDue);
				buyerDashboardDto.setTotalWeightedAmount(buyerDashboardDto.getTotalWeightedAmount()+(actualAmountDue * daysRemaining));					
			});
			buyerDashboardDto.setCurrencyName(invoiceCurrency.getCurrency());
			buyerDashboardDto.setCurrencySymbol(invoiceCurrency.getSymbol());
			try {
				buyerDashboardDto.setWatDays(buyerDashboardDto.getTotalWeightedAmount()/buyerDashboardDto.getTotalAmount());
			} catch (Exception e) {
				buyerDashboardDto.setWatDays(0);
			}
		} else {
			throw new Exception("No buyer company locations found");
		}
		return buyerDashboardDto;
	}

	private CurrencyDto getCompanyTransactionCurrency(long companyId) {
		List<CurrencyDto> currencyDtos = xpdCompanyRepo.getBuyerRelationships(companyId);
		if(currencyDtos != null && currencyDtos.size() > 0) {
			return currencyDtos.get(0);
		}
		return null;
	}

	@Override
	public List<InvoiceDto> getAmountDueLocationInvoices(long currentLocationId, String username) {
		XpdUser userObject = userRepo.findByUsername(username);
		this.lstAllInvoicesNotes = null;
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
		Set<XpdOrgMaster> companyLocations = null;
		if(currentLocationId == 0) {
			companyLocations = getCurrentCompany(username).getOrgMasters();
		}
		else {
			companyLocations = getCurrentCompany(username).getOrgMasters().stream().filter(orgMaster -> orgMaster.getId()==currentLocationId).collect(Collectors.toCollection(LinkedHashSet<XpdOrgMaster>::new));
		}
		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), getCurrentCompany(username).getId()).getNextPaymentDate();
//		Date newPaymentDate = invoiceService.getNewPaymentDate(getCurrentCompany(username).getId());
		List<Long> companyLocationIds = new ArrayList<Long>(); 
		companyLocations.forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		
		if (companyLocationIds.size() > 0) {
			List<XpdInvoiceLight> locationOpenInvoices = invoiceRepo.getLocationOpenInvoicesWithDateFilter(companyLocationIds, newPaymentDate);
			List<XpdInvoiceLight> locationRejectedInvoices = invoiceRepo.getLocationRejectedInvoicesWithDateFilter(companyLocationIds, newPaymentDate);
			locationOpenInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				currInvDto.setNotesSummary(this.getInvoiceNotes(invoice.getId(), invoice.getCurrency()));
				currInvDto.setActualAmountDue(this.getExactDueAmount(invoice.getId(), invoice.getTotalTrasactionValue()));
				resultInvoices.add(currInvDto);
			});
			locationRejectedInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				currInvDto.setNotesSummary(this.getInvoiceNotes(invoice.getId(), invoice.getCurrency()));
				currInvDto.setActualAmountDue(this.getExactDueAmount(invoice.getId(), invoice.getTotalTrasactionValue()));
				resultInvoices.add(currInvDto);
			});
		} else {
			logger.error("No buyer company locations found");
		}
		return resultInvoices;
	}

	@Override
	public List<InvoiceDto> getPaidInvoices(String location, String username, String supplier, String fromDate, String toDate, String invoiceNumber) throws ParseException {
		XpdUser userObject = userRepo.findByUsername(username);
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();

		supplier = "%"+supplier+"%";
		
		if (invoiceNumber.equals(""))
			invoiceNumber="%";
		else
			invoiceNumber = "%"+invoiceNumber+"%";
		
		Set<XpdOrgMaster> companyLocations = null;
		
		if(location.equals("All"))
			companyLocations = getCurrentCompany(username).getOrgMasters();
		else
			companyLocations = getCurrentCompany(username).getOrgMasters().stream().filter(orgMaster -> orgMaster.getName().equals(location)).collect(Collectors.toCollection(LinkedHashSet<XpdOrgMaster>::new));

		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(loc -> {
			companyLocationIds.add(loc.getId());
		});
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		fromDate = (fromDate.isEmpty())?fromDate:fromDate+" 00:00:01";
		toDate = (toDate.isEmpty())?toDate:toDate+" 23:59:59";
		
		Date sDate = (fromDate.isEmpty())?null:dateFormat.parse(fromDate);
		Date eDate = (toDate.isEmpty())?null:dateFormat.parse(toDate);
		
//		Calendar c = Calendar.getInstance(); 
//		c.setTime(eDate); 
//		c.add(Calendar.DATE, 1);
//		eDate = c.getTime();		
		
		List<XpdInvoiceLight> locationInvoices = null;
		
		if (companyLocationIds.size() > 0) {
			if (sDate == null && eDate == null)
				locationInvoices = invoiceRepo.getBuyerPaidInvoices(companyLocationIds, supplier, invoiceNumber);
			else if (sDate == null && eDate != null)
				locationInvoices = invoiceRepo.getBuyerPaidInvoicesWithEndDate(companyLocationIds, supplier, eDate, invoiceNumber);
			else if (sDate != null && eDate == null)
				locationInvoices = invoiceRepo.getBuyerPaidInvoicesWithStartDate(companyLocationIds, supplier, sDate, invoiceNumber);
			else
				locationInvoices = invoiceRepo.getBuyerPaidInvoicesWithStartEndDate(companyLocationIds, supplier, sDate, eDate, invoiceNumber);
			
			locationInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				resultInvoices.add(currInvDto);
			});
		} else {
			logger.error("No buyer company locations found");
		}
		return resultInvoices;
	}
	
	@Override
	public List<XpdInvoiceLight> getBuyerSummaryInvoices(String username, String fromDate, String toDate) throws ParseException {
		XpdUser userObject = userRepo.findByUsername(username);
		Set<XpdOrgMaster> companyLocations = null;
		companyLocations = getCurrentCompany(username).getOrgMasters();

		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(loc -> {
			companyLocationIds.add(loc.getId());
		});
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = (fromDate.isEmpty()) ? null : dateFormat.parse(fromDate);
		Date eDate = (toDate.isEmpty()) ? null : dateFormat.parse(toDate);
		List<XpdInvoiceLight> locationInvoices = null;
		if (companyLocationIds.size() > 0)
			locationInvoices = invoiceRepo.getBuyerInvoicesSummaryWithStartEndDate(companyLocationIds, sDate, eDate);
		else
			logger.error("No buyer company locations found");
		return locationInvoices;
	}
	

	@Override
	public List<InvoiceDto> getBuyerOpenInvoices(long currentLocationId, String username) {
		XpdUser userObject = userRepo.findByUsername(username);
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();
		this.lstAllInvoicesNotes = null;

//		Date newPaymentDate = dateRepo.getNewPaymentDate(new LocalDate().toDate(), getCurrentCompany(username).getId()).getNextPaymentDate();
		Date newPaymentDate = invoiceService.getNewPaymentDate(getCurrentCompany(username).getId());
		Set<XpdOrgMaster> companyLocations = null;
		if(currentLocationId == 0)
			companyLocations = getCurrentCompany(username).getOrgMasters();
		else
			companyLocations = getCurrentCompany(username).getOrgMasters().stream().filter(orgMaster -> orgMaster.getId()==currentLocationId).collect(Collectors.toCollection(LinkedHashSet<XpdOrgMaster>::new));

		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		if (companyLocationIds.size() > 0) {
			List<XpdInvoiceLight> locationInvoices = invoiceRepo.getBuyerOpenInvoices(companyLocationIds, newPaymentDate);
			locationInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				currInvDto.setNotesSummary(this.getInvoiceNotes(invoice.getId(), invoice.getCurrency()));
				resultInvoices.add(currInvDto);
			});
		} else {
			logger.error("No buyer company locations found");
		}
		return resultInvoices;
	}

	@Override
	public List<InvoiceDto> getUnderClearingInovoices(String username) {
		this.lstAllInvoicesNotes = null;
		XpdUser userObject = userRepo.findByUsername(username);
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();

		Set<XpdOrgMaster> companyLocations = null;
		companyLocations = getCurrentCompany(username).getOrgMasters();
		
		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		if (companyLocationIds.size() > 0) {
			List<XpdInvoiceLight> locationInvoices = invoiceRepo.getBuyerUnderApprovalInvoices(companyLocationIds);
			locationInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				currInvDto.setUserIsApprover(userObject.getIsApprover());
				currInvDto.setUserIsChecker(userObject.getIsChecker());
				currInvDto.setNotesSummary(this.getInvoiceNotes(invoice.getId(), invoice.getCurrency()));
				resultInvoices.add(currInvDto);
			});
		} else {
			logger.error("No buyer company locations found");
		}
		return resultInvoices;
	}
	
	@Override
	public List<InvoiceDto> getApprovalPendingInvoices(long currentLocationId, String username) {
		this.lstAllInvoicesNotes = null;
		XpdUser userObject = userRepo.findByUsername(username);
		List<InvoiceDto> resultInvoices = new ArrayList<InvoiceDto>();

		Set<XpdOrgMaster> companyLocations = null;
		if(currentLocationId == 0)
			companyLocations = getCurrentCompany(username).getOrgMasters();
		else
			companyLocations = getCurrentCompany(username).getOrgMasters().stream().filter(orgMaster -> orgMaster.getId()==currentLocationId).collect(Collectors.toCollection(LinkedHashSet<XpdOrgMaster>::new));
		
		List<Long> companyLocationIds = new ArrayList<Long>();
		companyLocations.forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		if (companyLocationIds.size() > 0) {
			List<XpdInvoiceLight> locationInvoices = invoiceRepo.getBuyerApprovalPendingInvoices(companyLocationIds);
			locationInvoices.forEach(invoice -> {
				InvoiceDto currInvDto = new InvoiceDto();
				currInvDto.setXpdInvoice(invoice);
				currInvDto.setUserIsApprover(userObject.getIsApprover());
				currInvDto.setUserIsChecker(userObject.getIsChecker());
				currInvDto.setNotesSummary(this.getInvoiceNotes(invoice.getId(), invoice.getCurrency()));
				resultInvoices.add(currInvDto);
			});
		} else {
			logger.error("No buyer company locations found");
		}
		return resultInvoices;
	}

	@Override
	synchronized public boolean approveInvoices(ApproveRejectRequestDto approveRequestDto) {
		logger.info("approveInvoices() : " + approveRequestDto.getLstInvoices().size() + " invoices are sent to approve");
		final XpdUser userObject = userRepo.findByUsername(approveRequestDto.getUsername());
		
		List<Long> companyLocationIds = new ArrayList<>();
		XpdCompany currentCompany = getCurrentCompany(approveRequestDto.getUsername());
		currentCompany.getOrgMasters().forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		emailContent = new StringBuffer();
		emailContent.append("<i>Dear <b>"+ currentCompany.getCompanyName() +"</b></i> !<br><br>"
				+ "The following invoices have been actioned upon by "+userObject.getUsername()+" on "+sdf.format(new Date())+".<br><br>"
				+"<table style='border: 1px solid black;border-collapse: collapse;text-align: center;display: block;width: auto; max-height: 200px;overflow-y: auto; border-style: none;'>"
				+ "<tr style='background-color: lightgrey'>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Supplier Name</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Invoice Number</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Amount Due<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Discount Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Net Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Original Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>New Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Status</th>"
        		+ "</tr>"
        		+ "");
		String smsMessage = "Dear " + currentCompany.getCompanyName() + ",\n";
				
		List<XpdStatus> allStatus = xpdStatusRepo.findAll();
		List<List<Long>> invoiceIds = new ArrayList<List<Long>>();
		invoiceIds.add(new ArrayList<>());
		int currentIndex = 0;
		List<XpdInvoiceStatus> newInvoiceStatuses = new ArrayList<XpdInvoiceStatus>();
		activeInvoiceStatuses = invoiceStatusRepo.getAllActiveInvoiceStatuses(companyLocationIds);

		Set<Long> uniqueSuppliers = new HashSet<Long>();
		
		for ( int count=0; count < approveRequestDto.getLstInvoices().size(); count++ ) {
			XpdInvoiceLight invoice = approveRequestDto.getLstInvoices().get(count);
			XpdInvoiceStatusLight currentStatus = activeInvoiceStatuses.stream().filter(invoiceStatus -> invoiceStatus.getInvoiceId() == invoice.getId()).findFirst().get();
			
			XpdStatus nextWorkFlowTask = allStatus.stream().filter(status -> status.getWorkflowSequence() == (currentStatus.getWorkFlowSequence()+1)).collect(Collectors.toList()).get(0);
			
			if(nextWorkFlowTask.getXpdDesc().equals(InvoiceStatusEnum.APPROVED.getValue())) {
				uniqueSuppliers.add(invoiceRepo.findOne(invoice.getId()).getXpdCompanyId().getId());
			}
			
			if (!nextWorkFlowTask.getXpdDesc().equals(InvoiceStatusEnum.POSTED_ON_ERP.getValue())) {
				XpdInvoice xpdInvoice = new XpdInvoice();
				xpdInvoice.setId(invoice.getId());
				XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
				newInvoiceStatus.setCreatedBy(userObject);
				newInvoiceStatus.setCreatedDate(new Date());
				newInvoiceStatus.setInvoiceId(xpdInvoice);
				newInvoiceStatus.setIsCurrent(1);
				newInvoiceStatus.setStatusId(nextWorkFlowTask);
				newInvoiceStatuses.add(newInvoiceStatus);

				
				
				
						NOI++;
					emailContent.append("<tr>"
							+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getCompanyName()+"</td>"
							+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getInvoiceNumber()+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDueAmount())+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDiscountAmount())+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getNetAmount())+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getDueDate() )+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getNewDueDate())+"</td>"
			        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'><font color=green><b>"+newInvoiceStatus.getStatusId().getXpdDesc()+"</b></font></td>"
			        		+ "</tr>");					
				
				if (invoiceIds.get(currentIndex).size() > 29999) {
					currentIndex ++;
					invoiceIds.add(currentIndex, new ArrayList<>());
				}
				invoiceIds.get(currentIndex).add(invoice.getId());
			}
		}
		
		invoiceIds.forEach(lstIds -> {
			invoiceStatusRepo.inactiveCurrentInvoiceStatus(lstIds);
		});
		
		invoiceStatusRepo.save(newInvoiceStatuses);
		uniqueSuppliers.forEach(currentSupplier -> {
			XpdCompany supplierCompany = xpdCompanyRepo.findOne(currentSupplier);
			String supplierEmailMessage = "Dear "+supplierCompany.getCompanyName()+",<br><br>"
					+ "Your invoices have been <font color='green'>APPROVED</font>.<br>"
					+ "Please log in to your <a href='https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com' target='_blank'>Xpedize Account</a> to view your <font color='green'>APPROVED</font> invoice(s).<br><br>";
//			String supplierSMSMessage = "APPROVED ! Dear "+supplierCompany.getCompanyName()+", "
//					+ "Your invoices have been APPROVED.Please log in to your Xpedize Account to view "
//					+ "your APPROVED invoices.\nhttps://jbml.xpedize.com";
			
			SendInvoiceEmailJob supplierEmailJob = new SendInvoiceEmailJob();
			supplierEmailJob.setMessage(supplierEmailMessage.toString());
			supplierEmailJob.setToAddress(supplierCompany.getEmail());
			supplierEmailJob.setSubject("Approved ! Your Early Payment Request On Xpedize");
			Thread emailThread = new Thread(supplierEmailJob);
			emailThread.start();
//			
//			SendSMSJob supplierSMSJob = new SendSMSJob();
//			try {
//				supplierSMSJob.setMessage(supplierSMSMessage);
//				supplierSMSJob.setTopicARN(userObject.getArn().toString());
//			} catch(Exception e) {
//				System.out.println("Error: "+e);
//			}
//			Thread job = new Thread(supplierSMSJob);
//			job.start();
		});

//		Commented Email and SMS sending Job below
		
		
		emailContent.append("</table>"
				+ "<br>"
				+ "To view further details, please login at https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com"
				+ "<br><br>"
				+ "For any assistance, you can directly reach out to us on info@xpedize.com or call on +91 9810325445 / +91 9820670506."
				+ "<br><br>"
				+ "");

		smsMessage+= NOI + " invoices have been actioned by "+userObject.getUsername()+" on "+sdf.format(new Date())+"."
				+ "Kindly visit https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com";

			SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
			emailJob.setMessage(emailContent.toString());
			emailJob.setToAddress(currentCompany.getEmail());
			emailJob.setSubject("Update ! Action Taken By User on Xpedize");
			Thread emailThread = new Thread(emailJob);
			emailThread.start();
			
			SendSMSJob smsJob = new SendSMSJob();
			try {
				smsJob.setMessage(smsMessage);
				smsJob.setTopicARN(userObject.getArn().toString());
			} catch(Exception e) {
				System.out.println("Error: "+e);
			}
			Thread job = new Thread(smsJob);
			job.start();
		
		emailContent=new StringBuffer();
		NOI=0;
		logger.info("approveInvoices() : " + approveRequestDto.getLstInvoices().size() + " invoices approved successfully");
		return true;
	}
	
	@Override
	synchronized public boolean postOnERPInvoices(ApproveRejectRequestDto postedOnERPDto, String comments) {
		logger.info("postOnERPInvoices() : " + postedOnERPDto.getLstInvoices().size() + " invoices are sent to post on ERP");
		final XpdUser userObject = userRepo.findByUsername(postedOnERPDto.getUsername());
		
		List<Long> companyLocationIds = new ArrayList<>();
		XpdCompany currentCompany = getCurrentCompany(postedOnERPDto.getUsername());
		currentCompany.getOrgMasters().forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		emailContent = new StringBuffer();
		emailContent.append("<i>Dear <b>"+ currentCompany.getCompanyName() +"</b></i> !<br><br>"
			+ "The following invoices have been actioned upon by "+userObject.getUsername()+" on "+sdf.format(new Date())+".<br><br>"
			+"<table style='border: 1px solid black;border-collapse: collapse;text-align: center;display: block;width: auto; max-height: 200px;overflow-y: auto; border-style: none;'>"
			+ "<tr style='background-color: lightgrey'>"
			+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Supplier Name</th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Invoice Number</th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Amount Due<div><small>(INR)</small></div></th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Discount Amount<div><small>(INR)</small></div></th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Net Amount<div><small>(INR)</small></div></th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Original Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>New Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
    		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Status</th>"
    		+ "</tr>"
    		+ "");
		String smsMessage = "Dear " + currentCompany.getCompanyName() + ",\n";
				
		List<XpdStatus> allStatus = xpdStatusRepo.findAll();
		List<List<Long>> invoiceIds = new ArrayList<List<Long>>();
		invoiceIds.add(new ArrayList<>());
		int currentIndex = 0;
		List<XpdInvoiceStatus> newInvoiceStatuses = new ArrayList<XpdInvoiceStatus>();

		Set<Long> uniqueSuppliers = new HashSet<Long>();
		
		Optional<XpdStatus> nextWorkFlowTask = allStatus.stream().filter(status -> status.getXpdDesc().equals(InvoiceStatusEnum.POSTED_ON_ERP.getValue())).findFirst();
		
		for ( int count=0; count < postedOnERPDto.getLstInvoices().size(); count++ ) {
			XpdInvoiceLight invoice = postedOnERPDto.getLstInvoices().get(count);
			XpdInvoice xpdInvoice = new XpdInvoice();
			XpdInvoiceStatus currentStatus = invoiceStatusRepo.getActiveInvoiceStatus(invoice.getId());
			if (!currentStatus.getStatusId().getXpdDesc().equals(InvoiceStatusEnum.POSTED_ON_ERP.getValue())) {
				xpdInvoice.setId(invoice.getId());
				XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
				newInvoiceStatus.setCreatedBy(userObject);
				newInvoiceStatus.setCreatedDate(new Date());
				newInvoiceStatus.setDescription(comments);
				newInvoiceStatus.setInvoiceId(xpdInvoice);
				newInvoiceStatus.setIsCurrent(1);
				newInvoiceStatus.setStatusId(nextWorkFlowTask.get());
				newInvoiceStatuses.add(newInvoiceStatus);
			
				NOI++;
				emailContent.append("<tr>"
						+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getCompanyName()+"</td>"
						+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getInvoiceNumber()+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDueAmount())+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDiscountAmount())+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getNetAmount())+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getDueDate() )+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getNewDueDate())+"</td>"
		        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'><font color=green><b>"+newInvoiceStatus.getStatusId().getXpdDesc()+"</b></font></td>"
		        		+ "</tr>");					
				
				if (invoiceIds.get(currentIndex).size() > 29999) {
					currentIndex ++;
					invoiceIds.add(currentIndex, new ArrayList<>());
				}
				invoiceIds.get(currentIndex).add(invoice.getId());
			}
		}
		if (invoiceIds != null && !invoiceIds.isEmpty() && !invoiceIds.get(0).isEmpty()) {
			invoiceIds.forEach(lstIds -> {
				invoiceStatusRepo.inactiveCurrentInvoiceStatus(lstIds);
			});
			invoiceStatusRepo.save(newInvoiceStatuses);
		
		
			uniqueSuppliers.forEach(currentSupplier -> {
				XpdCompany supplierCompany = xpdCompanyRepo.findOne(currentSupplier);
				String supplierEmailMessage = "Dear "+supplierCompany.getCompanyName()+",<br><br>"
						+ "Your invoices have been <font color='green'>POSTED ON ERP</font>.<br>"
						+ "Please log in to your <a href='https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com' target='_blank'>Xpedize Account</a> to view your <font color='green'>POSTED ON ERP</font> invoice(s).<br><br>";
				
				SendInvoiceEmailJob supplierEmailJob = new SendInvoiceEmailJob();
				supplierEmailJob.setMessage(supplierEmailMessage.toString());
				supplierEmailJob.setToAddress(supplierCompany.getEmail());
				supplierEmailJob.setSubject("Posted on ERP ! Your Early Payment Request On Xpedize");
				Thread emailThread = new Thread(supplierEmailJob);
				emailThread.start();
			});
	
	//		Commented Email and SMS sending Job below
			
			
			emailContent.append("</table>"
					+ "<br>"
					+ "To view further details, please login at https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com"
					+ "<br><br>"
					+ "For any assistance, you can directly reach out to us on info@xpedize.com or call on +91 9810325445 / +91 9820670506."
					+ "<br><br>"
					+ "");
	
			smsMessage+= NOI + " invoices have been actioned by "+userObject.getUsername()+" on "+sdf.format(new Date())+"."
					+ "Kindly visit https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com";
	
				SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
				emailJob.setMessage(emailContent.toString());
				emailJob.setToAddress(currentCompany.getEmail());
				emailJob.setSubject("Update ! Action Taken By User on Xpedize");
				Thread emailThread = new Thread(emailJob);
				emailThread.start();
				
				SendSMSJob smsJob = new SendSMSJob();
				try {
					smsJob.setMessage(smsMessage);
					smsJob.setTopicARN(userObject.getArn().toString());
				} catch(Exception e) {
					System.out.println("Error: "+e);
				}
				Thread job = new Thread(smsJob);
				job.start();
			
			emailContent=new StringBuffer();
			NOI=0;
			logger.info("approveInvoices() : " + postedOnERPDto.getLstInvoices().size() + " invoices successfully posted on ERP");
		}
		return true;
	}
	
	@Override
	synchronized public boolean payInvoices(ApproveRejectRequestDto paidInvoicesDto, String comments) {
		logger.info("payInvoices() : " + paidInvoicesDto.getLstInvoices().size() + " invoices are sent to get paid");
		final XpdUser userObject = userRepo.findByUsername(paidInvoicesDto.getUsername());
		
		List<Long> companyLocationIds = new ArrayList<>();
		XpdCompany currentCompany = getCurrentCompany(paidInvoicesDto.getUsername());
		currentCompany.getOrgMasters().forEach(location -> {
			companyLocationIds.add(location.getId());
		});
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		emailContent = new StringBuffer();
		emailContent.append("<i>Dear <b>"+ currentCompany.getCompanyName() +"</b></i> !<br><br>"
				+ "The following invoices have been actioned upon by "+userObject.getUsername()+" on "+sdf.format(new Date())+".<br><br>"
				+"<table style='border: 1px solid black;border-collapse: collapse;text-align: center;display: block;width: auto; max-height: 200px;overflow-y: auto; border-style: none;'>"
				+ "<tr style='background-color: lightgrey'>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Supplier Name</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Invoice Number</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Amount Due<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Discount Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Net Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Original Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>New Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Status</th>"
        		+ "</tr>"
        		+ "");
		String smsMessage = "Dear " + currentCompany.getCompanyName() + ",\n";
				
		List<XpdStatus> allStatus = xpdStatusRepo.findAll();
		List<List<Long>> invoiceIds = new ArrayList<List<Long>>();
		invoiceIds.add(new ArrayList<>());
		int currentIndex = 0;
		List<XpdInvoiceStatus> newInvoiceStatuses = new ArrayList<XpdInvoiceStatus>();

		Set<Long> uniqueSuppliers = new HashSet<Long>();
		
		Optional<XpdStatus> nextWorkFlowTask = allStatus.stream().filter(status -> status.getXpdDesc().equals(InvoiceStatusEnum.PAID.getValue())).findFirst();
		
		for ( int count=0; count < paidInvoicesDto.getLstInvoices().size(); count++ ) {
			XpdInvoiceLight invoice = paidInvoicesDto.getLstInvoices().get(count);
			
			XpdInvoice xpdInvoice = new XpdInvoice();
			xpdInvoice.setId(invoice.getId());
			XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
			newInvoiceStatus.setCreatedBy(userObject);
			newInvoiceStatus.setCreatedDate(new Date());
			newInvoiceStatus.setInvoiceId(xpdInvoice);
			newInvoiceStatus.setDescription(comments);
			newInvoiceStatus.setIsCurrent(1);
			newInvoiceStatus.setStatusId(nextWorkFlowTask.get());
			newInvoiceStatuses.add(newInvoiceStatus);
				
				NOI++;
			emailContent.append("<tr>"
					+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getCompanyName()+"</td>"
					+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+ invoice.getInvoiceNumber()+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDueAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDiscountAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getNetAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getDueDate() )+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getNewDueDate())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'><font color=green><b>"+newInvoiceStatus.getStatusId().getXpdDesc()+"</b></font></td>"
	        		+ "</tr>");					
			
			if (invoiceIds.get(currentIndex).size() > 29999) {
				currentIndex ++;
				invoiceIds.add(currentIndex, new ArrayList<>());
			}
			invoiceIds.get(currentIndex).add(invoice.getId());
		}
		
		if (invoiceIds != null && !invoiceIds.isEmpty() && !invoiceIds.get(0).isEmpty()) {
			invoiceIds.forEach(lstIds -> {
				invoiceStatusRepo.inactiveCurrentInvoiceStatus(lstIds);
			});
			
			invoiceStatusRepo.save(newInvoiceStatuses);
			
			uniqueSuppliers.forEach(currentSupplier -> {
				XpdCompany supplierCompany = xpdCompanyRepo.findOne(currentSupplier);
				String supplierEmailMessage = "Dear "+supplierCompany.getCompanyName()+",<br><br>"
						+ "Your invoices have been <font color='green'>POSTED ON ERP</font>.<br>"
						+ "Please log in to your <a href='https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com' target='_blank'>Xpedize Account</a> to view your <font color='green'>POSTED ON ERP</font> invoice(s).<br><br>";
				
				SendInvoiceEmailJob supplierEmailJob = new SendInvoiceEmailJob();
				supplierEmailJob.setMessage(supplierEmailMessage.toString());
				supplierEmailJob.setToAddress(supplierCompany.getEmail());
				supplierEmailJob.setSubject("Posted on ERP ! Your Early Payment Request On Xpedize");
				Thread emailThread = new Thread(supplierEmailJob);
				emailThread.start();
			});
	
	//		Commented Email and SMS sending Job below
			
			
			emailContent.append("</table>"
					+ "<br>"
					+ "To view further details, please login at https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com"
					+ "<br><br>"
					+ "For any assistance, you can directly reach out to us on info@xpedize.com or call on +91 9810325445 / +91 9820670506."
					+ "<br><br>"
					+ "");
	
			smsMessage+= NOI + " invoices have been actioned by "+userObject.getUsername()+" on "+sdf.format(new Date())+"."
					+ "Kindly visit https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com";
	
				SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
				emailJob.setMessage(emailContent.toString());
				emailJob.setToAddress(currentCompany.getEmail());
				emailJob.setSubject("Update ! Action Taken By User on Xpedize");
				Thread emailThread = new Thread(emailJob);
				emailThread.start();
				
				SendSMSJob smsJob = new SendSMSJob();
				try {
					smsJob.setMessage(smsMessage);
					smsJob.setTopicARN(userObject.getArn().toString());
				} catch(Exception e) {
					System.out.println("Error: "+e);
				}
				Thread job = new Thread(smsJob);
				job.start();
			
			emailContent=new StringBuffer();
			NOI=0;
			logger.info("approveInvoices() : " + paidInvoicesDto.getLstInvoices().size() + " invoices successfully posted on ERP");
		}
		return true;
	}

	@Override
	synchronized public boolean rejectInvoices(ApproveRejectRequestDto rejectRequestDto) {
		logger.info("rejectInvoices() : " + rejectRequestDto.getLstInvoices().size() + " invoices sent to reject");
		final XpdUser userObject = userRepo.findByUsername(rejectRequestDto.getUsername());
		XpdCompany currentCompany = getCurrentCompany(rejectRequestDto.getUsername());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		emailContent = new StringBuffer();
		emailContent.append("<i>Dear <b>"+ currentCompany.getCompanyName() +"</b></i> !<br><br>"
				+ "The following invoices have been actioned upon by "+userObject.getUsername()+" on "+sdf.format(new Date())+".<br><br>"
				+"<table style='border: 1px solid black;border-collapse: collapse;text-align: center;display: block;width: auto; max-height: 200px;overflow-y: auto; border-style: none;'>"
				+ "<tr style='background-color: lightgrey'>"
				+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Supplier Name</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Invoice Number</th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Amount Due<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Discount Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Net Amount<div><small>(INR)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Original Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>New Payment Date<div><small>(DD-MM-YYYY)</small></div></th>"
        		+ "<th style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>Status</th>"
        		+ "</tr>");
		
		String smsMessage = "Dear " + currentCompany.getCompanyName() + ",\n";
		
		List<List<Long>> invoiceIds = new ArrayList<List<Long>>();
		int currentIndex = 0;
		invoiceIds.add(new ArrayList());
		List<XpdInvoiceStatus> newInvoiceStatuses = new ArrayList<XpdInvoiceStatus>();
		XpdStatus nextWorkFlowTask = invoiceStatusRepo.getWorkflowStatusByStatusText(InvoiceStatusEnum.REJECTED.getValue());
		
		Set<Long> uniqueSuppliers = new HashSet<Long>();
		
		for ( int count=0; count <rejectRequestDto.getLstInvoices().size(); count++ ) {
			XpdInvoiceLight invoice = rejectRequestDto.getLstInvoices().get(count);
			if (invoiceIds.get(currentIndex).size() > 29999) {
				currentIndex ++;
				invoiceIds.add(currentIndex, new ArrayList<>());
			}
			invoiceIds.get(currentIndex).add(invoice.getId());
			
			XpdInvoice xpdInvoice = new XpdInvoice();
			xpdInvoice.setId(invoice.getId());
			XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
			newInvoiceStatus.setCreatedBy(userObject);
			newInvoiceStatus.setCreatedDate(new Date());
			newInvoiceStatus.setInvoiceId(xpdInvoice);
			newInvoiceStatus.setIsCurrent(1);
			newInvoiceStatus.setStatusId(nextWorkFlowTask);
			newInvoiceStatuses.add(newInvoiceStatus);
			
			uniqueSuppliers.add(invoiceRepo.findOne(invoice.getId()).getXpdCompanyId().getId());
			
//			String curr = invoice.getCurrency();
//			if(curr.equals("") || curr == null) {
//				curr = "";
//			}
				
			emailContent.append("<tr>"
					+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+invoice.getCompanyName()+"</td>"
					+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+invoice.getInvoiceNumber()+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDueAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getDiscountAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+Math.round(invoice.getNetAmount())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getDueDate() )+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'>"+sdf.format(invoice.getNewDueDate())+"</td>"
	        		+ "<td style='border: 1px solid black;border-collapse: collapse;padding: 0px 3px'><font color=red><b>Returned</b></font></td>"
	        		+ "</tr>");
			
		}
		
		invoiceIds.forEach(invIds -> {
			invoiceStatusRepo.inactiveCurrentInvoiceStatus(invIds);
		});
		invoiceStatusRepo.save(newInvoiceStatuses);
		uniqueSuppliers.forEach(currentSupplier -> {
			XpdCompany supplierCompany = xpdCompanyRepo.findOne(currentSupplier);
			String supplierEmailMessage = "Dear "+supplierCompany.getCompanyName()+",<br><br>"
					+ "Your invoices have been <font color='red'>RETURNED</font>.<br>"
					+ "Please log in to your <a href='https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com' target='_blank'>Xpedize Account</a> to view your <font color='red'>RETURNED</font> invoice(s).<br><br>"
					+ "You are requested to resubmit your bids for all Invoices ( NEW and RETURNED ) on the Xpedize platform.<br><br>"
					;
			
//			String supplierSMSMessage = "RETURNED ! Dear "+supplierCompany.getCompanyName()+", "
//					+ "Your invoices have been RETURNED.Please log in to your Xpedize Account to view "
//					+ "your RETURNED invoices.\nhttps://jbml.xpedize.com";
//			
			SendInvoiceEmailJob supplierEmailJob = new SendInvoiceEmailJob();
			supplierEmailJob.setMessage(supplierEmailMessage.toString());
			supplierEmailJob.setToAddress(supplierCompany.getEmail());
			supplierEmailJob.setSubject("RETURNED ! Your Early Payment Request On Xpedize");
			Thread emailThread = new Thread(supplierEmailJob);
			emailThread.start();
			
//			SendSMSJob supplierSMSJob = new SendSMSJob();
//			try {
//				supplierSMSJob.setMessage(supplierSMSMessage);
//				supplierSMSJob.setTopicARN(userObject.getArn().toString());
//			} catch(Exception e) {
//				System.out.println("Error: "+e);
//			}
//			Thread job = new Thread(supplierSMSJob);
//			job.start();
		});
		
		emailContent.append("</table>"
				+ "<br>"
				+ "<br>");
		
		smsMessage+="Dear "+currentCompany.getCompanyName()+", "
				+ rejectRequestDto.getLstInvoices().size() + " invoices have been actioned by "+userObject.getUsername()+" on "+sdf.format(new Date())+"."
				+ "Kindly visit https://"+poolConfig.getCurrentLoginDomain()+".xpedize.com"
				;
		
		if(rejectRequestDto.getLstInvoices().size() > 0) {
			SendInvoiceEmailJob emailJob = new SendInvoiceEmailJob();
			emailJob.setMessage(emailContent.toString());
			emailJob.setToAddress(currentCompany.getEmail());
			emailJob.setSubject("Update ! Action Taken By User on Xpedize");
			Thread emailThread = new Thread(emailJob);
			emailThread.start();
			
			SendSMSJob smsJob = new SendSMSJob();
			try {
				smsJob.setMessage(smsMessage);
				smsJob.setTopicARN(userObject.getArn().toString());
			} catch(Exception e) {
				System.out.println("Error: "+e);
			}
			Thread job = new Thread(smsJob);
			job.start();
		}
		
		emailContent = new StringBuffer();
		logger.info("rejectInvoices() : " + rejectRequestDto.getLstInvoices().size() + " invoices rejected successfully");
		return true;
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
	
	private String getInvoiceNotes(long invoiceId, String currency) {
		StringBuffer notesBuffer = new StringBuffer("");
		if (this.lstAllInvoicesNotes == null) {
			this.lstAllInvoicesNotes = invoiceNotesRepo.findAllNotes("XPD%");
		}
		List<XpdInvoiceNoteLight> invoiceNotes = lstAllInvoicesNotes.stream().filter(note -> note.getInvoiceId()==invoiceId).collect(Collectors.toList());
		for (XpdInvoiceNoteLight note : invoiceNotes) {
			SimpleDateFormat fd =new SimpleDateFormat("dd-MM-yyyy");
			String dd = fd.format(note.getCreatedDate());
			notesBuffer.append("Date: "+dd+" | Type: "+note.getNoteDesc() + " | Amount: "+currency+" " + note.getTotalTrasactionValue() + "\n");
		}
		return notesBuffer.toString();
	}
	
	public List<XpdCompanyOfferDto> getSuppliers(String username) {
		List<XpdCompanyOfferDto> lstCompanyOffers = new ArrayList<XpdCompanyOfferDto>();
		try {
			XpdCompany currentCompany = this.getCurrentCompany(username);
			List<XpdCompany> lstSuppliers = companyRelationshipRepo.getSupplierCompaniesForBuyer(currentCompany.getId());
			List<XpdCompanyOffer> offers = companyOfferRepo.findBuyerCompanyOffers(currentCompany.getId());
			lstSuppliers.forEach(supplier -> {
				XpdCompanyOfferDto offerDto = new XpdCompanyOfferDto();
				offerDto.setId(supplier.getId());
				offerDto.setCompanyName(supplier.getCompanyName());
				offerDto.setCompanyDescription(supplier.getDescription());
				
				Optional<XpdCompanyOffer> foundOffer = offers.stream().filter(offer -> offer.getXpdCompanyId().getId()==supplier.getId()).findFirst();
				if(foundOffer.isPresent()) {
					offerDto.setConstantPercentage(Double.parseDouble(foundOffer.get().getXpdOffer().getOfferCriterias().get(0).getValue()));
					offerDto.setOfferType(foundOffer.get().getXpdOffer().getOfferCriterias().get(0).getSysOfferParameterId());
					if (foundOffer.get().getXpdOffer().getOfferCriterias().get(0).getSysOfferParameterId().getParameter().equals(AppConstants.BUYER_MINIMUM_OFFER_TYPE)) {
						offerDto.setMaxPercentage(foundOffer.get().getXpdOffer().getOfferCriterias().get(0).getMaxPercentage().doubleValue());
						offerDto.setMinPercentage(foundOffer.get().getXpdOffer().getOfferCriterias().get(0).getMinPercentage().doubleValue());
					}
					offerDto.setOfferEndDate(foundOffer.get().getEndDate());
					offerDto.setOfferStartDate(foundOffer.get().getStartDate());
				}
				lstCompanyOffers.add(offerDto);
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return lstCompanyOffers;
	}
	
	public boolean saveSupplierOffers(String username, Long offerTypeId, List<XpdCompany> supplierCompanies,
			Double constantPercentage, Double minPercentage, Double maxPercentage, String offerStartDate, String offerEndDate) throws Exception {
		try {
			XpdCompany currentCompany = this.getCurrentCompany(username);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date fromDate = (offerStartDate == null || offerStartDate.trim().isEmpty())?null:dateFormat.parse(offerStartDate);
			Date toDate = (offerEndDate == null || offerEndDate.trim().isEmpty())?null:dateFormat.parse(offerEndDate);
			SysOfferParameter offerParameter = offerParameterRepo.findOne(offerTypeId);
			for (XpdCompany supplier : supplierCompanies) {
				SysOfferType sysOfferType = xpdOfferRepo.getBuyerOfferType();
				XpdOfferCriteria criteria = new XpdOfferCriteria();
				if (offerParameter.getDescription().equals(InvoiceUtil.FIXED_OFFER_TYPE)) {
					criteria.setValue(constantPercentage.toString());
				} else {
					criteria.setMinPercentage(new BigDecimal(minPercentage));
					criteria.setMaxPercentage(new BigDecimal(maxPercentage));
					criteria.setValue(minPercentage.toString());
				}
				XpdOffer offer = new XpdOffer();
				offer.setTargetAveragePercentage(new BigDecimal(constantPercentage));
				offer.setSysOfferTypeId(sysOfferType);
				xpdOfferRepo.save(offer);
				
				criteria.setIsCurrent(true);
				criteria.setXpdOffer(offer);
				criteria.setSysOfferParameterId(offerParameter);
				criteriaRepo.save(criteria);
				
				companyOfferRepo.inactiveCurrentCompanyOffer(currentCompany.getId(), supplier.getId());

				XpdCompanyOffer companyOffer = new XpdCompanyOffer();
				companyOffer.setIsCurrent(true);
				companyOffer.setXpdBuyerCompanyId(currentCompany);
				companyOffer.setXpdCompanyId(supplier);
				companyOffer.setXpdOffer(offer);
				companyOffer.setStartDate(fromDate);
				companyOffer.setEndDate(toDate);
				companyOfferRepo.save(companyOffer);
				
			}
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}

	@Override
	public boolean companyIsSupplier(String username) {
		XpdCompany currentCompany = getCurrentCompany(username);
		List<XpdCompanyRelationship> lstSupplierRelationshipList = xpdCompanyRepo.isSupplierCompany(currentCompany.getId());
		return (lstSupplierRelationshipList != null && lstSupplierRelationshipList.size()>0);
	}
	
	private XpdCompany getCurrentCompany(String username) {
		XpdCompany sessionCompany = userController.getCurrentCompany(username);
		return xpdCompanyRepo.findOne(sessionCompany.getId());
	}
}
