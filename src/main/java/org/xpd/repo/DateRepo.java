package org.xpd.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdNewPaymentDates;

@Repository
public interface DateRepo extends JpaRepository<XpdNewPaymentDates, Long> {

	@Query("FROM XpdNewPaymentDates where curentDate=?1 and buyerCompany.id=?2")
	XpdNewPaymentDates getNewPaymentDate(Date currentDate, long buyerCompanyId);

	@Query("FROM XpdNewPaymentDates where curentDate=?1")
	XpdNewPaymentDates getNewPaymentDate(Date currentDate);
	
}
