package org.xpd.models.dto;

import java.util.List;

import org.xpd.models.XpdCompany;

public class BuyerOfferDto {
	String username;
    Long offerTypeId;
    List<XpdCompany> supplierCompanies;
    Double constantPercentage;
    Double minPercentage;
    Double maxPercentage;
    String offerStartDate;
    String offerEndDate;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<XpdCompany> getSupplierCompanies() {
		return supplierCompanies;
	}
	public Long getOfferTypeId() {
		return offerTypeId;
	}
	public void setOfferTypeId(Long offerTypeId) {
		this.offerTypeId = offerTypeId;
	}
	public void setSupplierCompanies(List<XpdCompany> supplierCompanies) {
		this.supplierCompanies = supplierCompanies;
	}
	public Double getConstantPercentage() {
		return constantPercentage;
	}
	public void setConstantPercentage(Double constantPercentage) {
		this.constantPercentage = constantPercentage;
	}
	public Double getMinPercentage() {
		return minPercentage;
	}
	public void setMinPercentage(Double minPercentage) {
		this.minPercentage = minPercentage;
	}
	public Double getMaxPercentage() {
		return maxPercentage;
	}
	public void setMaxPercentage(Double maxPercentage) {
		this.maxPercentage = maxPercentage;
	}
	public String getOfferStartDate() {
		return offerStartDate;
	}
	public void setOfferStartDate(String offerStartDate) {
		this.offerStartDate = offerStartDate;
	}
	public String getOfferEndDate() {
		return offerEndDate;
	}
	public void setOfferEndDate(String offerEndDate) {
		this.offerEndDate = offerEndDate;
	} 
    
    
}
