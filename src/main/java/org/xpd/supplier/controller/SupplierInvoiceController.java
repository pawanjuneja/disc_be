package org.xpd.supplier.controller;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.dto.XpdOfferDto;
import org.xpd.supplier.dto.InvoiceDto;
import org.xpd.supplier.dto.InvoiceOfferDto;
import org.xpd.supplier.dto.SupplierDashboardDto;
import org.xpd.supplier.dto.UserMobileDto;
import org.xpd.supplier.service.IInvoiceService;

@RestController
@RequestMapping(value="/supplier")
@CrossOrigin(origins="*")
public class SupplierInvoiceController {
	
	@Autowired
	IInvoiceService invoiceService;
	
	@RequestMapping(value="/getCompanyLocations")
	public List<XpdOrgMaster> getCompanyLocations(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.getCompanyLocations(username);
	}
	
//	Get Buyer Companies
	@RequestMapping(value="/getBuyerCompanies")
	public List<XpdCompany> getBuyerCompanies(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.getBuyerCompanies(username);
	}
	
//	Get Specific Buyer Company Locations
	
//	@RequestMapping(value="/getSpecificBuyerLocations")
//	public List<XpdOrgMaster> getSpecificBuyerCompanyLocations(HttpServletRequest request, @RequestParam String username, @RequestParam long buyerId) {
//		return invoiceService.getSpecificBuyerCompanyLocations(username, buyerId);
//	}
	
	@RequestMapping(value="/getBuyerLocations")
	public List<XpdOrgMaster> getBuyerLocations(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.getBuyerLocations(username);
	}
	
	@RequestMapping(value="/getLocationInvoices") 
	public List<InvoiceDto> getLocationOpenInvoices(HttpServletRequest request, @RequestParam String username, 
														@RequestParam long currentLocationId,
														@RequestParam long buyerCompanyId) {
		return invoiceService.getLocationOpenInvoices(currentLocationId, buyerCompanyId, username);
	}
	
	@RequestMapping(value="/getAllCompanyInvoices") 
	public List<InvoiceDto> getAllCompanyOpenInvoices(HttpServletRequest request, @RequestParam String username) throws ParseException {
		String authtoken = request.getHeader("Authorization");
		return invoiceService.getAllCompanyOpenInvoices(username);
	}
	
	@RequestMapping(value="/getSupplierDashboardData") 
	public SupplierDashboardDto getSupplierDashboardData(HttpServletRequest request, @RequestParam String username, @RequestParam long buyerId) {
		return invoiceService.getSupplierDashboardData(username, buyerId);
	}
	
	@RequestMapping(value="/submitInvoiceOffer", method=RequestMethod.POST)
	public Boolean submitInvoiceOffer(HttpServletRequest request, @RequestBody InvoiceOfferDto invoiceOfferDto) {
		return invoiceService.submitInvoiceOffer(invoiceOfferDto);
	}
	
	@RequestMapping(value="/getApprovedInvoices") 
	public List<InvoiceDto> getApprovedInvoices(HttpServletRequest request, 
							@RequestParam(required=false) String username, 
							@RequestParam(required=false) String buyerId, 
							@RequestParam(required=false) String location, 
							@RequestParam(required=false) String fromDate,
							@RequestParam(required=false) String toDate,
							@RequestParam(required=false) String invoiceNumber) throws ParseException {
		return invoiceService.getApprovedInvoices(username,buyerId, location, fromDate, toDate, invoiceNumber);
	}
	
	@RequestMapping(value="/getUnderClearingLocationInvoices") 
	public List<InvoiceDto> getUnderClearingLocationInvoices(HttpServletRequest request, @RequestParam long buyerId,@RequestParam long currentLocationId, @RequestParam(required=false) String username) {
		return invoiceService.getLocationUnderClearingInvoices(buyerId,currentLocationId, username);
	}
	
	@RequestMapping(value="/getCompanyInvoiceOffer") 
	public XpdOfferDto getCompanyInvoiceOffer(HttpServletRequest request, @RequestParam String username, @RequestParam long buyerId) throws ParseException {
		return invoiceService.getCompanyInvoiceOffer(username, buyerId);
	}
	
	@RequestMapping(value="/getNextDueDate")
	public Date getNewDueDate(HttpServletRequest request) {
		LocalDate todaysDate = LocalDate.now();
		Instant instant = new Date().toInstant();
		LocalDate startDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate endDate = todaysDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		if (daysBetween < 3)
			endDate = endDate.plusWeeks(1);
		System.out.println("days between " + daysBetween);
		
		return java.sql.Date.valueOf(endDate);
	}
	
	@RequestMapping(value="/otp")
	public Boolean generateOTP(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.generateOTP(username);
	}
	
	@RequestMapping(value="/checkOTP")
	public Boolean checkOTP(HttpServletRequest request, @RequestParam String username, String otp) {
		return invoiceService.checkOTPIsValid(username, otp);
	}
	
	@RequestMapping(value="/authDetails")
	public Boolean isAuth(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.getAuth(username);
	}
	
	@RequestMapping(value="/verfiyPan")
	public Boolean verfiyPAN(HttpServletRequest request, @RequestParam String username,@RequestParam String nPAN) {
		return invoiceService.checkPAN(username, nPAN);
	}
	
	@RequestMapping(value="/saveMob")
	public Boolean saveMob(HttpServletRequest request, @RequestBody UserMobileDto userMobDto) {
		return invoiceService.saveMobileNumber(userMobDto.getUsername(), userMobDto.getMobileNumber());
	}
	
	@RequestMapping(value="/checkRole")
	public Boolean CheckUserRole(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.checkUserRole(username);
	}
	
	@RequestMapping(value="/changePasswordNotification")
	public boolean changePasswordNotification(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.sendPasswordChangeEmail(username);
	}
	
	@RequestMapping(value="/companyIsBuyer")
	public boolean companyIsBuyer(HttpServletRequest request, @RequestParam String username) {
		return invoiceService.companyIsBuyer(username);
	}
	
}
