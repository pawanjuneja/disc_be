package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdPayments;

@Repository
public interface XpdPaymentsRepo extends JpaRepository<XpdPayments, Long> {

	@Query("FROM XpdPayments where xpdInvoiceId.id=?1")
	public XpdPayments getPaymentDetailsOfInvoice(long invoiceId);
	
	@Query("FROM XpdPayments where isUpdated=false")
	public List<XpdPayments> getNonUpdatedPayments();
}

