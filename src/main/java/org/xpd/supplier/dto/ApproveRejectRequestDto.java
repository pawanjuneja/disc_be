package org.xpd.supplier.dto;

import java.io.Serializable;
import java.util.List;

import org.xpd.models.XpdInvoice;
import org.xpd.models.dto.XpdInvoiceLight;

public class ApproveRejectRequestDto implements Serializable {
	private List<XpdInvoiceLight> lstInvoices;
	private boolean approve;
	private String username;
	
	public List<XpdInvoiceLight> getLstInvoices() {
		return lstInvoices;
	}
	public void setLstInvoices(List<XpdInvoiceLight> lstInvoices) {
		this.lstInvoices = lstInvoices;
	}
	public boolean isApprove() {
		return approve;
	}
	public void setApprove(boolean approve) {
		this.approve = approve;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
