package org.xpd.supplier.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.xpd.models.XpdInvoice;

public class InvoiceOfferDto implements Serializable {
	private List<XpdInvoice> lstInvoices;
	private Date nextDueDate;
	private BigDecimal selectedPrice;
	private String username;
	
	public List<XpdInvoice> getLstInvoices() {
		return lstInvoices;
	}
	public void setLstInvoices(List<XpdInvoice> lstInvoices) {
		this.lstInvoices = lstInvoices;
	}
	public Date getNextDueDate() {
		return nextDueDate;
	}
	public void setNextDueDate(Date nextDueDate) {
		this.nextDueDate = nextDueDate;
	}
	public BigDecimal getSelectedPrice() {
		return selectedPrice;
	}
	public void setSelectedPrice(BigDecimal selectedPrice) {
		this.selectedPrice = selectedPrice;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
