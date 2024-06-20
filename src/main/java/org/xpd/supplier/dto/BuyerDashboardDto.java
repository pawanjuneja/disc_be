package org.xpd.supplier.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BuyerDashboardDto implements Serializable {
	
	private Double totalSavingsAchievedAmount;
	private double aprPercentage;
	private Double approvalPendingAmount;
	private Double underClearingAmount;
	private Double openInvoicesTotalAmount;
	private double watDays;
	private String currencySymbol;
	private String currencyName;
	private int totalPaidInvoices;
	private Double totalAmount;
	private Double totalWeightedAmount;
	public Double getTotalSavingsAchievedAmount() {
		return totalSavingsAchievedAmount;
	}
	public void setTotalSavingsAchievedAmount(Double totalSavingsAchievedAmount) {
		this.totalSavingsAchievedAmount = totalSavingsAchievedAmount;
	}
	public double getAprPercentage() {
		return aprPercentage;
	}
	public void setAprPercentage(double aprPercentage) {
		this.aprPercentage = aprPercentage;
	}
	public Double getApprovalPendingAmount() {
		return approvalPendingAmount;
	}
	public void setApprovalPendingAmount(Double approvalPendingAmount) {
		this.approvalPendingAmount = approvalPendingAmount;
	}
	public Double getUnderClearingAmount() {
		return underClearingAmount;
	}
	public void setUnderClearingAmount(Double underClearingAmount) {
		this.underClearingAmount = underClearingAmount;
	}
	public Double getOpenInvoicesTotalAmount() {
		return openInvoicesTotalAmount;
	}
	public void setOpenInvoicesTotalAmount(Double openInvoicesTotalAmount) {
		this.openInvoicesTotalAmount = openInvoicesTotalAmount;
	}
	public double getWatDays() {
		return watDays;
	}
	public void setWatDays(double watDays) {
		this.watDays = watDays;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public int getTotalPaidInvoices() {
		return totalPaidInvoices;
	}
	public void setTotalPaidInvoices(int totalPaidInvoices) {
		this.totalPaidInvoices = totalPaidInvoices;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalWeightedAmount() {
		return totalWeightedAmount;
	}
	public void setTotalWeightedAmount(Double totalWeightedAmount) {
		this.totalWeightedAmount = totalWeightedAmount;
	}
	
	
	
}
