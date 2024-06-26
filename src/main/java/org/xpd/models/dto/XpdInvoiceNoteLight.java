package org.xpd.models.dto;

import java.util.Date;

// Generated 20 Aug, 2018 7:46:07 PM by Hibernate Tools 5.3.1.Final

/**
 * Xpdinvoicenote generated by hbm2java
 */
public class XpdInvoiceNoteLight implements java.io.Serializable {

	private Double totalTrasactionValue;
	private String noteDesc;
	private Date createdDate;
	private long invoiceId;
	
	public XpdInvoiceNoteLight(Double totalTransactionValue, String noteDesc, Date createdDate) {
		this.totalTrasactionValue = totalTransactionValue;
		this.noteDesc = noteDesc;
		this.createdDate = createdDate;
	}
	
	public XpdInvoiceNoteLight(Double totalTransactionValue, String noteDesc, Date createdDate, Long invoiceId) {
		this.totalTrasactionValue = totalTransactionValue;
		this.noteDesc = noteDesc;
		this.createdDate = createdDate;
		this.invoiceId = invoiceId;
	}
	
	public Double getTotalTrasactionValue() {
		return totalTrasactionValue;
	}
	public void setTotalTrasactionValue(Double totalTrasactionValue) {
		this.totalTrasactionValue = totalTrasactionValue;
	}
	public String getNoteDesc() {
		return noteDesc;
	}
	public void setNoteDesc(String noteDesc) {
		this.noteDesc = noteDesc;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}
