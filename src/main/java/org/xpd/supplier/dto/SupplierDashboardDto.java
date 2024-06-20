package org.xpd.supplier.dto;

import java.io.Serializable;

public class SupplierDashboardDto implements Serializable {
	
	private double daysReceivedEarly;
	
	private double amountDiscountedTillDate;
	
	private double amountUnderClearing;
	
	private int numberOfInvoicesUnderClearning;
	
	private double totalAmount;
	
	private int totalInvoices;
	
	private String currencySymbol;
	
	public SupplierDashboardDto() {
	
	}
	public SupplierDashboardDto(Double totalAmount, Long totalInvoices) {
		this.totalAmount = totalAmount;
		this.totalInvoices = totalInvoices.intValue();
	}

	public double getAmountUnderClearing() {
		return amountUnderClearing;
	}

	public void setAmountUnderClearing(double amountUnderClearing) {
		this.amountUnderClearing = amountUnderClearing;
	}

	public int getNumberOfInvoicesUnderClearning() {
		return numberOfInvoicesUnderClearning;
	}

	public void setNumberOfInvoicesUnderClearning(int numberOfInvoicesUnderClearning) {
		this.numberOfInvoicesUnderClearning = numberOfInvoicesUnderClearning;
	}

	public int getTotalInvoices() {
		return totalInvoices;
	}

	public void setTotalInvoices(int totalInvoices) {
		this.totalInvoices = totalInvoices;
	}

	public double getAmountDiscountedTillDate() {
		return amountDiscountedTillDate;
	}

	public void setAmountDiscountedTillDate(double amountDiscountedTillDate) {
		this.amountDiscountedTillDate = amountDiscountedTillDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public double getDaysReceivedEarly() {
		return daysReceivedEarly;
	}

	public void setDaysReceivedEarly(double daysReceivedEarly) {
		this.daysReceivedEarly = daysReceivedEarly;
	}
}
