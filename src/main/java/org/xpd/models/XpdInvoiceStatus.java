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

@Entity
@Table
public class XpdInvoiceStatus implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdInvoice invoiceId;
	
	@ManyToOne
	private XpdStatus statusId;
	
	@Column
	private long isCurrent;
	
	@Column
	private String description;
	
	@ManyToOne
	private XpdUser createdBy;
	
	@Column
	private Date createdDate;
	
	@ManyToOne
	private XpdUser modifiedBy;
	
	@Column
	private Date modifiedDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public XpdInvoice getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(XpdInvoice invoiceId) {
		this.invoiceId = invoiceId;
	}

	public XpdStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(XpdStatus statusId) {
		this.statusId = statusId;
	}

	public long getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(long isCurrent) {
		this.isCurrent = isCurrent;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
