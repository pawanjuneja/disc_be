package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdCompanyRelationship;
import org.xpd.models.dto.CurrencyDto;

@Repository
public interface XpdCompanyRepo extends JpaRepository<XpdCompany, Long> {

	@Query("Select new org.xpd.models.dto.CurrencyDto(sysCurrencyId.currency, sysCurrencyId.symbol) From XpdCompanyRelationship where xpdcompanyByXpdSupplierCompanyId.id=?1")
	public List<CurrencyDto> getSupplierRelationships(long supplierId);
	
	@Query("Select new org.xpd.models.dto.CurrencyDto(sysCurrencyId.currency, sysCurrencyId.symbol) From XpdCompanyRelationship where xpdcompanyByXpdBuyerCompanyId.id=?1")
	public List<CurrencyDto> getBuyerRelationships(long buyerId);
	
	@Query("Select new org.xpd.models.XpdCompanyRelationship(id) From XpdCompanyRelationship where xpdcompanyByXpdBuyerCompanyId.id=?1")
	public List<XpdCompanyRelationship> isBuyerCompany(long companyId);
	
	@Query("Select new org.xpd.models.XpdCompanyRelationship(id) From XpdCompanyRelationship where xpdcompanyByXpdSupplierCompanyId.id=?1")
	public List<XpdCompanyRelationship> isSupplierCompany(long companyId);
	
	@Query("from XpdCompany where companyName = ?1")
	public XpdCompany getCompanyByName(String name);
	
}
