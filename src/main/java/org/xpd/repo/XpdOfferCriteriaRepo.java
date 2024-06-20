package org.xpd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdOfferCriteria;

@Repository
public interface XpdOfferCriteriaRepo extends JpaRepository<XpdOfferCriteria, Long> {
	
	
//	@Query ("FROM XpdOfferCriteria WHERE xpdCompanyId.id=?1 AND xpdOfferId.xpdCompanyId.id=?2 AND xpdOfferId.annualPercentage=?3")
//	public XpdOfferCriteria findBuyerOfferCriteriaByPercentage(long xpdBuyerId, long xpdSupplierId, double annualPercentage);
//	
//	@Query ("FROM XpdOfferCriteria WHERE xpdCompanyId.id=?2 AND xpdOfferId.xpdCompanyId.id=?1 and xpdOfferId.sysOfferTypeId.offerType='Buyer-Offer'")
//	public XpdOfferCriteria findBuyerOfferCriteria(long xpdBuyerId, long xpdSupplierId);
}
