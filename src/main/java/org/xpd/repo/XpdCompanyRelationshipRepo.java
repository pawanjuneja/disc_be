package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdCompanyRelationship;

public interface XpdCompanyRelationshipRepo extends JpaRepository<XpdCompanyRelationship, Long> {
	
	@Query("select xpdcompanyByXpdBuyerCompanyId from XpdCompanyRelationship rel where rel.xpdcompanyByXpdSupplierCompanyId.id=?1 and rel.isBlacklisted=false")
	public List<XpdCompany> getBuyersCompaniesForSupplier(long supplierId);
	
	@Query("select xpdcompanyByXpdSupplierCompanyId from XpdCompanyRelationship rel where rel.xpdcompanyByXpdBuyerCompanyId.id=?1 and rel.isBlacklisted=false")
	public List<XpdCompany> getSupplierCompaniesForBuyer(long buyerId);
}
