package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.XpdCompanyOffer;

@Repository
public interface XpdCompanyOfferRepo extends JpaRepository<XpdCompanyOffer, Long> {
	
	@Query("from XpdCompanyOffer where xpdCompanyId.id=?1 and xpdBuyerCompanyId.id=?2 and isCurrent=true")
	public XpdCompanyOffer findBuyerCompanyOffer(long xpdSupplierId, long buyerId);
	
	@Query("from XpdCompanyOffer where xpdBuyerCompanyId.id=?1 and isCurrent=true")
	public List<XpdCompanyOffer> findBuyerCompanyOffers(long buyerId);
	
	@Transactional
	@Modifying (clearAutomatically=true)
	@Query("Update XpdCompanyOffer set isCurrent=false where xpdBuyerCompanyId.id=:buyerId and xpdCompanyId.id=:supplierId")
	void inactiveCurrentCompanyOffer(@Param("buyerId") Long buyerId, @Param("supplierId") Long supplierId);
	
}
