package org.xpd.supplier.dto;

import java.io.Serializable;
import java.util.Date;

import org.xpd.models.dto.XpdInvoiceLight;

public class InvoiceDto implements Serializable {

	private XpdInvoiceLight xpdInvoice;
	private boolean userIsApprover;
	private boolean userIsChecker;
	private Date newPaymentDate;
	private String notesSummary;
	private String lastApprover;
	private double actualAmountDue;
	
	public XpdInvoiceLight getXpdInvoice() {
		return xpdInvoice;
	}
	public void setXpdInvoice(XpdInvoiceLight xpdInvoice) {
		this.xpdInvoice = xpdInvoice;
	}
	public boolean isUserIsApprover() {
		return userIsApprover;
	}
	public void setUserIsApprover(boolean userIsApprover) {
		this.userIsApprover = userIsApprover;
	}
	public boolean isUserIsChecker() {
		return userIsChecker;
	}
	public void setUserIsChecker(boolean userIsChecker) {
		this.userIsChecker = userIsChecker;
	}
	public Date getNewPaymentDate() {
		return newPaymentDate;
	}
	public void setNewPaymentDate(Date newPaymentDate) {
		this.newPaymentDate = newPaymentDate;
	}
	public String getNotesSummary() {
		return notesSummary;
	}
	public void setNotesSummary(String notesSummary) {
		this.notesSummary = notesSummary;
	}
	public String getLastApprover() {
		return lastApprover;
	}
	public void setLastApprover(String lastApprover) {
		this.lastApprover = lastApprover;
	}
	public double getActualAmountDue() {
		return actualAmountDue;
	}
	public void setActualAmountDue(double actualAmountDue) {
		this.actualAmountDue = actualAmountDue;
	}
}
