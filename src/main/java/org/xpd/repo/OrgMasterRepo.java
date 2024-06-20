package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdCompany;
import org.xpd.models.XpdOrgMaster;

@Repository
public interface OrgMasterRepo extends JpaRepository<XpdOrgMaster, Long> {
	
}
