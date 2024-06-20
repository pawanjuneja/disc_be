package org.xpd.models.dto;

import java.util.Date;

public class XpdInvoiceLight implements java.io.Serializable {

	
	private long id;
	private String invoiceNumber;
	private Date invoiceDate;
	private Double totalTrasactionValue;
	private Date dueDate;
	private String companyName;
	private String supplierDesc;
	private String buyerDesc;
	private String xpdDesc;
	private String currency;
	
	//invoice offer
	private double annualPercentage;
	private double discountAmount;
	private double daysRemaining;
	private double discountPercent30Days;
	private double appliedDiscount;
	private double netAmount;
	private Date newDueDate;
	private double dueAmount;
	private Date clearingDate;
	private Date submissionDate;
	private String orgMasterName;
	private String lastApprover;
	
	public XpdInvoiceLight() {
		
	}
	
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String orgMasterName) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.orgMasterName = orgMasterName;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, Double discountPercent30Days, String supplierDesc, String currency) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.discountPercent30Days = discountPercent30Days;
		this.supplierDesc = supplierDesc;
		this.currency = currency;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String companyName, String orgMasterName) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.orgMasterName = orgMasterName;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String companyName, String currency, String orgMasterName) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.currency = currency;
		this.orgMasterName = orgMasterName;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String companyName, String currency,
			String supplierDesc, String buyerDesc, String xpdDesc, double annualPercentage, double discountAmount, double daysRemaining, double discountPercent30Days, double appliedDiscount, 
			double netAmount, Date newDueDate, double dueAmount, Date submissionDate, String orgMasterName, String lastApprover) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.currency = currency;
		this.supplierDesc = supplierDesc;
		this.buyerDesc = buyerDesc;
		this.xpdDesc = xpdDesc;
		
		this.annualPercentage = annualPercentage;
		this.discountAmount = discountAmount;
		this.daysRemaining = daysRemaining;
		this.discountPercent30Days = discountPercent30Days;
		this.appliedDiscount = appliedDiscount;
		this.netAmount = netAmount;
		this.newDueDate = newDueDate;
		this.dueAmount = dueAmount;
		this.submissionDate = submissionDate;
		this.orgMasterName = orgMasterName;
		this.lastApprover = lastApprover;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String companyName, String currency,
			String supplierDesc, String buyerDesc, String xpdDesc, double annualPercentage, double discountAmount, double daysRemaining, double discountPercent30Days, double appliedDiscount, 
			double netAmount, Date newDueDate, double dueAmount, Date submissionDate, String orgMasterName) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.currency = currency;
		this.supplierDesc = supplierDesc;
		this.buyerDesc = buyerDesc;
		this.xpdDesc = xpdDesc;
		
		this.annualPercentage = annualPercentage;
		this.discountAmount = discountAmount;
		this.daysRemaining = daysRemaining;
		this.discountPercent30Days = discountPercent30Days;
		this.appliedDiscount = appliedDiscount;
		this.netAmount = netAmount;
		this.newDueDate = newDueDate;
		this.dueAmount = dueAmount;
		this.submissionDate = submissionDate;
		this.orgMasterName = orgMasterName;
	}
	
	public XpdInvoiceLight(long id, String invoiceNumber, Date invoiceDate, Double totalTrasactionValue, Date dueDate, String companyName, String currency,
			String supplierDesc, String buyerDesc, String xpdDesc, double annualPercentage, double discountAmount, double daysRemaining, double discountPercent30Days, double appliedDiscount, 
			double netAmount, Date newDueDate, double dueAmount, Date clearingDate, Date submissionDate, String orgMasterName) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.currency = currency;
		this.supplierDesc = supplierDesc;
		this.buyerDesc = buyerDesc;
		this.xpdDesc = xpdDesc;
		
		this.annualPercentage = annualPercentage;
		this.discountAmount = discountAmount;
		this.daysRemaining = daysRemaining;
		this.discountPercent30Days = discountPercent30Days;
		this.appliedDiscount = appliedDiscount;
		this.netAmount = netAmount;
		this.newDueDate = newDueDate;
		this.dueAmount = dueAmount;
		this.clearingDate = clearingDate;
		this.submissionDate = submissionDate;
		this.orgMasterName = orgMasterName;
	}
	
	public Date getClearingDate() {
		return clearingDate;
	}

	public void setClearingDate(Date clearingDate) {
		this.clearingDate = clearingDate;
	}

	public XpdInvoiceLight(long id, 
						   String invoiceNumber, 
						   Date invoiceDate, 
						   Double totalTrasactionValue, 
						   Date dueDate, 
						   String companyName,
						   String supplierDesc,
						   String buyerDesc,
						   String xpdDesc) {
		this.id = id;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.totalTrasactionValue = totalTrasactionValue;
		this.dueDate = dueDate;
		this.companyName = companyName;
		this.supplierDesc = supplierDesc;
		this.buyerDesc = buyerDesc;
		this.xpdDesc = xpdDesc;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Double getTotalTrasactionValue() {
		return totalTrasactionValue;
	}
	public void setTotalTrasactionValue(Double totalTrasactionValue) {
		this.totalTrasactionValue = totalTrasactionValue;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSupplierDesc() {
		return supplierDesc;
	}

	public void setSupplierDesc(String supplierDesc) {
		this.supplierDesc = supplierDesc;
	}

	public String getBuyerDesc() {
		return buyerDesc;
	}

	public void setBuyerDesc(String buyerDesc) {
		this.buyerDesc = buyerDesc;
	}

	public String getXpdDesc() {
		return xpdDesc;
	}

	public void setXpdDesc(String xpdDesc) {
		this.xpdDesc = xpdDesc;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAnnualPercentage() {
		return annualPercentage;
	}

	public void setAnnualPercentage(double annualPercentage) {
		this.annualPercentage = annualPercentage;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(double daysRemaining) {
		this.daysRemaining = daysRemaining;
	}

	public double getDiscountPercent30Days() {
		return discountPercent30Days;
	}

	public void setDiscountPercent30Days(double discountPercent30Days) {
		this.discountPercent30Days = discountPercent30Days;
	}

	public double getAppliedDiscount() {
		return appliedDiscount;
	}

	public void setAppliedDiscount(double appliedDiscount) {
		this.appliedDiscount = appliedDiscount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public Date getNewDueDate() {
		return newDueDate;
	}

	public void setNewDueDate(Date newDueDate) {
		this.newDueDate = newDueDate;
	}

	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getOrgMasterName() {
		return orgMasterName;
	}

	public void setOrgMasterName(String orgMasterName) {
		this.orgMasterName = orgMasterName;
	}


	public String getLastApprover() {
		return lastApprover;
	}


	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}
	
}
