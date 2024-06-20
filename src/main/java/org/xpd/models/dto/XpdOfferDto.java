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

/**
 * @author pawanjuneja
 */
public class XpdOfferDto implements java.io.Serializable {

	private long id;
	private String offerName;
	private Date startDate;
	private Date endDate;
	private BigDecimal processingDays;
	private BigDecimal annualPercentage;
	private BigDecimal minPercentage;
	private BigDecimal maxPercentage;
	private BigDecimal targetAveragePercentage;
	private Double auctionTargetAmmount;
	private long createdBy;
	private Date createdDate;
	private long modifiedBy;
	private Date modifiedDate;
	private Double netAmount;
	private boolean fixedOffer;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getProcessingDays() {
		return processingDays;
	}
	public void setProcessingDays(BigDecimal processingDays) {
		this.processingDays = processingDays;
	}
	public BigDecimal getAnnualPercentage() {
		return annualPercentage;
	}
	public void setAnnualPercentage(BigDecimal annualPercentage) {
		this.annualPercentage = annualPercentage;
	}
	public BigDecimal getMinPercentage() {
		return minPercentage;
	}
	public void setMinPercentage(BigDecimal minPercentage) {
		this.minPercentage = minPercentage;
	}
	public BigDecimal getMaxPercentage() {
		return maxPercentage;
	}
	public void setMaxPercentage(BigDecimal maxPercentage) {
		this.maxPercentage = maxPercentage;
	}
	public BigDecimal getTargetAveragePercentage() {
		return targetAveragePercentage;
	}
	public void setTargetAveragePercentage(BigDecimal targetAveragePercentage) {
		this.targetAveragePercentage = targetAveragePercentage;
	}
	public Double getAuctionTargetAmmount() {
		return auctionTargetAmmount;
	}
	public void setAuctionTargetAmmount(Double auctionTargetAmmount) {
		this.auctionTargetAmmount = auctionTargetAmmount;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public boolean isFixedOffer() {
		return fixedOffer;
	}
	public void setFixedOffer(boolean fixedOffer) {
		this.fixedOffer = fixedOffer;
	}
	
	

}
