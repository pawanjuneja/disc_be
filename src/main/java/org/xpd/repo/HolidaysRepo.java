package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdHoliday;

@Repository
public interface HolidaysRepo extends JpaRepository<XpdHoliday, Long> {
	
	@Query("From XpdHoliday holiday where holiday.company.id=?1")
	public List<XpdHoliday> getHolidaysForCompany(long companyId);
	
//	@Query("From XpdHoliday holiday where holiday.holidayId=?1")
//	public List<XpdHoliday> getHolidaysForHolidayId(String holidayId);
	
}
