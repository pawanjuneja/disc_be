package org.xpd.buyer.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpd.buyer.service.IBuyerInvoiceService;
import org.xpd.models.SysOfferParameter;
import org.xpd.models.XpdCompany;
import org.xpd.models.dto.BuyerOfferDto;
import org.xpd.models.dto.XpdCompanyOfferDto;
import org.xpd.models.dto.XpdInvoiceLight;
import org.xpd.supplier.dto.ApproveRejectRequestDto;
import org.xpd.supplier.dto.BuyerDashboardDto;
import org.xpd.supplier.dto.InvoiceDto;

@RestController
@RequestMapping(value="/buyer")
@CrossOrigin(origins="*")
public class BuyerInvoiceController {
	
	@Autowired
	IBuyerInvoiceService buyerInvoiceService;
	
	@RequestMapping(value="/getAmountDueLocationInvoices") 
	public List<InvoiceDto> getUnderClearingLocationInvoices(HttpServletRequest request, @RequestParam long currentLocationId, @RequestParam(required=false) String username) {
		return buyerInvoiceService.getAmountDueLocationInvoices(currentLocationId, username);
	}
	
	@RequestMapping(value="/getPaidInvoices")
	public List<InvoiceDto> getPaidInvoices(HttpServletRequest request, 
											@RequestParam String location, 
											@RequestParam String username,
											@RequestParam String supplier,
											@RequestParam String fromDate,
											@RequestParam String toDate,
											@RequestParam String invoiceNumber)  throws ParseException {
		return buyerInvoiceService.getPaidInvoices(location, username, supplier, fromDate, toDate, invoiceNumber);
	}
	
	@RequestMapping(value="/getApprovalPendingInvoices") 
	public List<InvoiceDto> getLocationOpenInvoices(HttpServletRequest request, @RequestParam long currentLocationId, @RequestParam String username) {
		return buyerInvoiceService.getApprovalPendingInvoices(currentLocationId, username);
	}

	@RequestMapping(value="/getBuyerDashboardData") 
	public BuyerDashboardDto getSupplierDashboardData(HttpServletRequest request, @RequestParam String username) throws Exception {
		return buyerInvoiceService.getBuyerDashboardData(username);
	}
	
	@RequestMapping(value="/approveInvoices", method=RequestMethod.POST)
	public boolean approveInvoices(HttpServletRequest request, @RequestBody ApproveRejectRequestDto approveRequestDto) {
		return buyerInvoiceService.approveInvoices(approveRequestDto);
	}
	
	@RequestMapping(value="/rejectInvoices", method=RequestMethod.POST)
	public boolean rejectInvoices(HttpServletRequest request, @RequestBody ApproveRejectRequestDto rejectRequestDto) {
		return buyerInvoiceService.rejectInvoices(rejectRequestDto);
	}
	
	@RequestMapping(value="/getSummaryInvoices")
	public List<XpdInvoiceLight> getSummaryInvoices(HttpServletRequest request, 
			@RequestParam String username,
			@RequestParam String fromDate,
			@RequestParam String toDate) throws ParseException {
		return buyerInvoiceService.getBuyerSummaryInvoices(username, fromDate, toDate);
	}
	
	@RequestMapping(value="/companyIsSupplier")
	public boolean companyIsSupplier(HttpServletRequest request, @RequestParam String username) {
		return buyerInvoiceService.companyIsSupplier(username);
	}
	
	@RequestMapping (value="/getSuppliers") 
	public List<XpdCompanyOfferDto> getSuppliers(HttpServletRequest request, @RequestParam String username) {
		return buyerInvoiceService.getSuppliers(username);
	}
	
	@RequestMapping (value="/saveSupplierOffers")
	public boolean saveSupplierOffers(HttpServletRequest request, @RequestBody BuyerOfferDto buyerOfferDto) throws Exception {
		return buyerInvoiceService.saveSupplierOffers(buyerOfferDto.getUsername(), buyerOfferDto.getOfferTypeId(), buyerOfferDto.getSupplierCompanies(), 
				buyerOfferDto.getConstantPercentage(), buyerOfferDto.getMinPercentage(), buyerOfferDto.getMaxPercentage(), 
				buyerOfferDto.getOfferStartDate(), buyerOfferDto.getOfferEndDate());
	}
	
	@RequestMapping (value="/getUnderClearingInovoices")
	public List<InvoiceDto> getUnderClearingInovoices(HttpServletRequest request, @RequestParam String username) {
		return buyerInvoiceService.getUnderClearingInovoices(username);
	}
	
	@RequestMapping (value="/postOnERPInvoices")
	public boolean postOnERPInvoices(HttpServletRequest request, 
			@RequestBody ApproveRejectRequestDto approveRequestDto,
			@RequestParam String comments) {
		return buyerInvoiceService.postOnERPInvoices(approveRequestDto, comments);
	}
	
	@RequestMapping (value="/payInvoices")
	public boolean payInvoices(HttpServletRequest request, 
			@RequestBody ApproveRejectRequestDto approveRequestDto, 
			@RequestParam String comments) {
		return buyerInvoiceService.payInvoices(approveRequestDto, comments);
	}
	
	@RequestMapping (value="/getAllOfferTypes")
	public List<SysOfferParameter> payInvoices(HttpServletRequest request) {
		return buyerInvoiceService.getAllOfferTypes();
	}
}
