package org.xpd.models.dto;

import java.io.Serializable;

public class XpdInvoiceStatusLight implements Serializable {

	private long id;
	
	private int workFlowSequence;
	
	private long invoiceId;

	public XpdInvoiceStatusLight() {
		
	}
	
	public XpdInvoiceStatusLight(long id, int workFlowSequence, long invoiceId) {
		this.id = id;
		this.workFlowSequence = workFlowSequence;
		this.invoiceId = invoiceId;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getWorkFlowSequence() {
		return workFlowSequence;
	}

	public void setWorkFlowSequence(int workFlowSequence) {
		this.workFlowSequence = workFlowSequence;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	
}
