package org.xpd.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author pawanjuneja
 *
 */
@Entity
@Table
public class XpdStatus implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private String supplierDesc;
	
	@Column
	private String buyerDesc;
	
	@Column
	private String xpdDesc;
	
	@ManyToOne
	private XpdUser createdBy;
	
	@Column
	private Date createdDate;
	
	@ManyToOne
	private XpdUser modifiedBy;
	
	@Column
	private Date modifiedDate;
	
	@Column
	private int workflowSequence;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		return xpdDesc.trim();
	}

	public void setXpdDesc(String xpdDesc) {
		this.xpdDesc = xpdDesc;
	}

	public XpdUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(XpdUser createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public XpdUser getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(XpdUser modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getWorkflowSequence() {
		return workflowSequence;
	}

	public void setWorkflowSequence(int workflowSequence) {
		this.workflowSequence = workflowSequence;
	}
}
