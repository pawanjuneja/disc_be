package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.XpdUser;

@Repository
public interface UserRepo extends JpaRepository<XpdUser, Long> {
	
	@Query("FROM XpdUser u where u.username=?1")
	XpdUser findByUsername(String username);
	
	@Query("FROM XpdUser u where u.isActive=true")
	List<XpdUser> findAllActiveUsers();
	
	@Transactional
	@Modifying (clearAutomatically=true)
	@Query("Update XpdUser set isActive=false where id in (:userIds)")
	void inactiveUsers(@Param("userIds") List<Long> userIdsIds);
}
