package org.xpd.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.SysOfferParameter;
import org.xpd.models.SysOfferType;
import org.xpd.models.XpdInvoiceOffer;
import org.xpd.models.XpdOffer;

@Repository
public interface XpdOfferRepo extends JpaRepository<XpdOffer, Long> {

	@Query("From SysOfferType where offerType='Supplier-Offer'")
	SysOfferType getSupplierOfferType();
	
	@Query("From SysOfferType where offerType='Buyer-Offer'")
	SysOfferType getBuyerOfferType();
	
	@Query("From SysOfferParameter where parameter='Buyer-Fixed-Offer'")
	SysOfferParameter getBuyerFixedOfferParameter();
	
	@Query("From SysOfferParameter where parameter='Buyer-Minimum-Offer'")
	SysOfferParameter getBuyerVariableOfferParameter();

}
