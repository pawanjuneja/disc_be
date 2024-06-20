package org.xpd.supplier.dto;

public enum NoteTypeEnum {

	ADVANCE_PAYMENT("Advance-Payment"),
	INVOICE("Invoice"),
	CREDIT_MEMO("Credit-Memo"),
	DEBIT_MEMO("Debit-Memo"),
	PAYMENT("Payment");
	
	private String value;
	
	private NoteTypeEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}
