package org.xpd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdStatus;

@Repository
public interface XpdStatusRepo extends JpaRepository<XpdStatus, Long> {

	
}
