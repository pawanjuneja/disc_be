package org.xpd.models.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.xpd.models.SysOfferParameter;

/**
 * @author pawanjuneja
 */
public class XpdCompanyOfferDto implements java.io.Serializable {

	private long id;	
	private String companyName;
	private String companyDescription;
	private SysOfferParameter offerType;
	private Double constantPercentage;
	private Double minPercentage;
	private Double maxPercentage;
	private Date offerStartDate;
	private Date offerEndDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyDescription() {
		return companyDescription;
	}
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	public Double getConstantPercentage() {
		return constantPercentage;
	}
	public SysOfferParameter getOfferType() {
		return offerType;
	}
	public void setOfferType(SysOfferParameter offerType) {
		this.offerType = offerType;
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
	public Date getOfferStartDate() {
		return offerStartDate;
	}
	public void setOfferStartDate(Date offerStartDate) {
		this.offerStartDate = offerStartDate;
	}
	public Date getOfferEndDate() {
		return offerEndDate;
	}
	public void setOfferEndDate(Date offerEndDate) {
		this.offerEndDate = offerEndDate;
	}
	
	
	
}
