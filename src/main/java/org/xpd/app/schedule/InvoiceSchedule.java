package org.xpd.app.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xpd.models.XpdInvoiceOffer;
import org.xpd.models.XpdInvoiceStatus;
import org.xpd.models.XpdPayments;
import org.xpd.models.XpdStatus;
import org.xpd.models.XpdUser;
import org.xpd.repo.InvoiceOfferRepo;
import org.xpd.repo.InvoiceStatusRepo;
import org.xpd.repo.UserRepo;
import org.xpd.repo.XpdPaymentsRepo;
import org.xpd.supplier.dto.InvoiceStatusEnum;

@Component
public class InvoiceSchedule {

//	@Autowired
//	XpdPaymentsRepo paymentsRepo;
//	
	@Autowired
	UserRepo userRepo;
//	
//	@Autowired
//	InvoiceStatusRepo invoiceStatusRepo;
//	
//	@Autowired
//	InvoiceOfferRepo invoiceOfferRepo;
	
//	 To run this scheduled job every day at 1:00 AM		
//	@Scheduled (cron="0 0 01 * * *")
	
//	To run this scheduled job after every 5 mins.
//	@Scheduled (fixedDelay=18000)

	
	@Scheduled (cron="0 0 01 * * *")
	public void invoiceUpdateJob() {
		XpdUser testUser = userRepo.findByUsername("SuperUser");
		System.out.println("SCHEDULED JOB IS COMMENTED :-"+testUser.getUsername());
		//Updating invoices with payment status
//		List<XpdPayments> lstPayments = paymentsRepo.getNonUpdatedPayments();
//		for (XpdPayments currentPayment : lstPayments) {
//			XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
//			newInvoiceStatus.setCreatedBy(testUser);
//			newInvoiceStatus.setCreatedDate(new Date());
//			newInvoiceStatus.setInvoiceId(currentPayment.getXpdInvoiceId());
//			newInvoiceStatus.setIsCurrent(1);
//			XpdStatus paidStatus = invoiceStatusRepo.getWorkflowStatusByStatusText(InvoiceStatusEnum.PAID.getValue());
//			newInvoiceStatus.setStatusId(paidStatus);
//			XpdInvoiceStatus currentInvoiceStatus = invoiceStatusRepo.getActiveInvoiceStatus(currentPayment.getXpdInvoiceId().getId());
//			currentInvoiceStatus.setIsCurrent(0);
//			invoiceStatusRepo.save(currentInvoiceStatus);
//			invoiceStatusRepo.save(newInvoiceStatus);
//			currentPayment.setIsUpdated(true);
//			paymentsRepo.save(currentPayment);			
//		}
		
		//Updating invoices to Rejected status if not approved till new due date
//		List<XpdInvoiceOffer> lstLapsedOffers = invoiceOfferRepo.getLapsedInvoices(new Date());
//		XpdStatus rejectedStatus = invoiceStatusRepo.getWorkflowStatusByStatusText(InvoiceStatusEnum.REJECTED.getValue());
//		for (XpdInvoiceOffer invoiceOffer : lstLapsedOffers) {
//			XpdInvoiceStatus currentInvoiceStatus = invoiceStatusRepo.getActiveInvoiceStatus(invoiceOffer.getXpdInvoiceId().getId());
//			if (currentInvoiceStatus.getStatusId().getXpdDesc().equalsIgnoreCase(InvoiceStatusEnum.PENDING_FOR_VERIFICATION.getValue()) ||
//				currentInvoiceStatus.getStatusId().getXpdDesc().equalsIgnoreCase(InvoiceStatusEnum.PENDING_FOR_APPROVAL.getValue()) ||
//				currentInvoiceStatus.getStatusId().getXpdDesc().equalsIgnoreCase(InvoiceStatusEnum.WAITING_FOR_APPROVAL_2.getValue())) {
//					XpdInvoiceStatus newInvoiceStatus = new XpdInvoiceStatus();
//					newInvoiceStatus.setCreatedBy(testUser);
//					newInvoiceStatus.setCreatedDate(new Date());
//					newInvoiceStatus.setInvoiceId(invoiceOffer.getXpdInvoiceId());
//					newInvoiceStatus.setIsCurrent(1);
//					newInvoiceStatus.setStatusId(rejectedStatus);
//					currentInvoiceStatus.setIsCurrent(0);
//					invoiceStatusRepo.save(currentInvoiceStatus);
//					invoiceStatusRepo.save(newInvoiceStatus);
//			}
//		}
	}
}
