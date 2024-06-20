package org.xpd.supplier.dto;

public enum InvoiceStatusEnum {

	AVAILABLE("Open"),
	PENDING_FOR_VERIFICATION("Requested by Supplier"),
	PENDING_FOR_APPROVAL("Verified by Checker"),
	WAITING_FOR_APPROVAL_2("Approved by Level 1"),
	REJECTED("Rejected"),
	APPROVED("Approved"),
	POSTED_ON_ERP("Posted on ERP"),
	PAID("Paid");

	private String value;
	
	private InvoiceStatusEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}
