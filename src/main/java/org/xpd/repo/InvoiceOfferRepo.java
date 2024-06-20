package org.xpd.repo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.XpdInvoiceOffer;

@Repository
public interface InvoiceOfferRepo extends JpaRepository<XpdInvoiceOffer, Long> {

	@Query("FROM XpdInvoiceOffer offer WHERE offer.xpdInvoiceId.id=?1 order by id desc")
	List<XpdInvoiceOffer> getInvoiceOffers(long invoiceId);
	
	@Query("SELECT sum(discountAmount) FROM XpdInvoiceOffer offer WHERE offer.xpdOfferId.id=?1 order by id desc")
	double getInProgressInvoicesTotalDiscountAmount(String xpdDesc);
	
	@Query("FROM XpdInvoiceOffer offer WHERE offer.xpdInvoiceId.xpdCompanyId.id=?1")
	List<XpdInvoiceOffer> getAllInvoicesOffersForXpdCompany(long xpdCompanyId);
	
	@Query("FROM XpdInvoiceOffer offer WHERE offer.xpdCompanyId.id=?1 and offer.xpdOfferId.annualPercentage=?2")
	List<XpdInvoiceOffer> getExistingInvoiceOffer(long xpdCompanyId, BigDecimal annualPercentage);
	
	@Query("FROM XpdInvoiceOffer offer WHERE offer.newDueDate < ?1")
	List<XpdInvoiceOffer> getLapsedInvoices(Date currentDate);
	
	@Query("FROM XpdInvoiceOffer offer where offer.xpdInvoiceId.id=?1")
	List<XpdInvoiceOffer> fetchLastInvoiceOffers(long invoiceId);
	
	@Query("FROM XpdInvoiceOffer offer WHERE offer.xpdInvoiceId.id=?1 and offer.isActive=true")
	XpdInvoiceOffer getActiveInvoiceOffer(long invoiceId);
	
	@Query("Select new org.xpd.models.XpdInvoiceOffer(id, netAmount, dueAmount, discountAmount, newDueDate) FROM XpdInvoiceOffer offer WHERE offer.isActive=true")
	List<XpdInvoiceOffer> getAllActiveInvoiceOffers();
	
	@Transactional
	@Modifying (clearAutomatically=true)
	@Query("Update XpdInvoiceOffer set isActive=false where xpdInvoiceId.id in (:invoiceIds)")
	void inactiveCurrentInvoiceOffer(@Param("invoiceIds") List<Long> invoiceIds);
	
}
