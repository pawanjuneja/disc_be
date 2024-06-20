package org.xpd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.XpdPaymentDateStrategy;

@Repository
public interface PaymentStrategyRepo extends JpaRepository<XpdPaymentDateStrategy, Long> {
	
	@Query("FROM XpdPaymentDateStrategy strategy where strategy.company.id=?1 and strategy.isActive=true")
	public XpdPaymentDateStrategy findPaymentStrategyForCompany(long companyId); 	
	
	@Transactional
	@Modifying (clearAutomatically=true)
	@Query("Update XpdPaymentDateStrategy set isActive=false where company.id=?1")
	void inactiveCurrentPaymentStrategy(Long companyId);
}