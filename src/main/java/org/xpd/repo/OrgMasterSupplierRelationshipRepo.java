package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.XpdOrgMasterSupplierRelationship;

@Repository
public interface OrgMasterSupplierRelationshipRepo extends JpaRepository<XpdOrgMasterSupplierRelationship, Long> {

	@Query("FROM XpdOrgMasterSupplierRelationship orgRel where orgRel.xpdOrgMasterId.id=?1")
	List<XpdOrgMasterSupplierRelationship> getSupplierRelationshipForOrgMaster(long orgMasterId);
	
}
