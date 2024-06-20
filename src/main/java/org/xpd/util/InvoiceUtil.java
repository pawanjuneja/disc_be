package org.xpd.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.xpd.models.XpdPaymentDateStrategy;
import org.xpd.supplier.dto.InvoiceStatusEnum;

public class InvoiceUtil {
	
	public static String FIXED_OFFER_TYPE = "Fixed";
	
	public static String VARIABLE_OFFER_TYPE = "Variable";
	
	public static String BUYER_OFFER_TYPE_DESCRIPTION = "Buyer-Initiated-Offer";
	
	public static String PAYMENT_DATE_STRATEGY_DAYS = "SpecifiedDays";
	
	public static String PAYMENT_DATE_STRATEGY_WEEKDAY = "DayOfWeek";
	
	
	public static boolean isInvoiceUnderProcessing(String xpdStatusDesc) {
		return (xpdStatusDesc.equals(InvoiceStatusEnum.PENDING_FOR_APPROVAL.getValue())
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.PENDING_FOR_VERIFICATION.getValue())
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.WAITING_FOR_APPROVAL_2.getValue())
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.APPROVED.getValue())
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.POSTED_ON_ERP.getValue()));
	}
	
	public static String getUnderProcessingStatuses() {
		return ("'"+InvoiceStatusEnum.PENDING_FOR_APPROVAL.getValue()+"','"+
				InvoiceStatusEnum.PENDING_FOR_VERIFICATION.getValue()+"','"+
				InvoiceStatusEnum.WAITING_FOR_APPROVAL_2.getValue()+"','"+
				InvoiceStatusEnum.APPROVED.getValue()+"','"+
				InvoiceStatusEnum.POSTED_ON_ERP.getValue()+"'");
	}
	
	public static boolean isInvoiceApprovedAndNotPaid(String xpdStatusDesc) {
		return (xpdStatusDesc.equals(InvoiceStatusEnum.APPROVED.getValue())
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.POSTED_ON_ERP.getValue()));
	}
	
	public static boolean isInvoiceInApproval(String xpdStatusDesc) {
		return (xpdStatusDesc.equals(InvoiceStatusEnum.PENDING_FOR_APPROVAL.getValue()) 
				|| xpdStatusDesc.trim().equals(InvoiceStatusEnum.WAITING_FOR_APPROVAL_2.getValue()));
	}
	
	
	public static boolean isCompleted(String xpdStatusDesc) {
		return (xpdStatusDesc.trim().equals(InvoiceStatusEnum.PAID.getValue()));
	}
	
	public static boolean isRejected(String xpdStatusDesc) {
		return (xpdStatusDesc.trim().equals(InvoiceStatusEnum.REJECTED.getValue()));
	}
	
	public static boolean isOpenInvoice(String xpdStatusDesc) {
		return (xpdStatusDesc.trim().equals(InvoiceStatusEnum.AVAILABLE.getValue()));
	}
	
	public static long getDaysBetween(Date startDate, Date endDate) {
		Instant instant = startDate.toInstant();
        LocalDate sDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        Instant endDateInstant = endDate.toInstant();
        LocalDate eDate = endDateInstant.atZone(ZoneId.systemDefault()).toLocalDate();
		long daysBetween = ChronoUnit.DAYS.between(sDate, eDate);
		return daysBetween;
	}
}
