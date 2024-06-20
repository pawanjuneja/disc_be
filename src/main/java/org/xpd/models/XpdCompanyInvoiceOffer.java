package org.xpd.models;
// Generated 20 Aug, 2018 7:46:07 PM by Hibernate Tools 5.3.1.Final

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author pawanjuneja
 */
@Entity
@Table
public class XpdCompanyInvoiceOffer implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdCompany xpdCompanyId;
	
	@ManyToOne
	private XpdOrgMaster xpdOrgMasterId;
	
	@ManyToOne
	private XpdOffer xpdOffer;

	private Date startDate;
	private Date endDate;
	private boolean isCurrent;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public XpdCompany getXpdCompanyId() {
		return xpdCompanyId;
	}
	public void setXpdCompanyId(XpdCompany xpdCompanyId) {
		this.xpdCompanyId = xpdCompanyId;
	}
	public XpdOrgMaster getXpdOrgMasterId() {
		return xpdOrgMasterId;
	}
	public void setXpdOrgMasterId(XpdOrgMaster xpdOrgMasterId) {
		this.xpdOrgMasterId = xpdOrgMasterId;
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
	public boolean getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public XpdOffer getXpdOffer() {
		return xpdOffer;
	}
	public void setXpdOffer(XpdOffer xpdOffer) {
		this.xpdOffer = xpdOffer;
	}
}
