package org.xpd.app.controller;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xpd.models.XpdHoliday;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.XpdPaymentDateStrategy;
import org.xpd.models.XpdUser;
import org.xpd.models.dto.XpdHolidayDto;
import org.xpd.repo.HolidaysRepo;
import org.xpd.repo.PaymentStrategyRepo;

@RestController
@RequestMapping(value="/holiday")
@CrossOrigin(origins = "*")
public class HolidayController {

	Logger logger = LoggerFactory.getLogger(HolidayController.class);
	
	@Autowired
	HolidaysRepo holidaysRepo;
	
	@Autowired
	PaymentStrategyRepo paymentStrategyRepo;
	
	@Autowired
	UserController userController;
	
	@RequestMapping(value="/saveHoliday")
	public boolean saveHoliday(HttpServletRequest request, 
								@RequestBody XpdHoliday holiday, 
								@RequestParam String username) {
		try {
			XpdUser userObject = userController.getUserByUsername(username);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			String holidayId = (dateFormat.format(holiday.getHolidayDate())+holiday.getHolidayDescription());
			if (holiday.getLocations().size() == 0 || holiday.getLocations().size() == holiday.getCompany().getOrgMasters().size()) {
				holiday.setIsNationalHoliday(true);
				holiday.setLocations(new ArrayList<XpdOrgMaster>());
			}
			holiday.setHolidayId(holidayId);
			holiday.setCreatedBy(userObject);
			holiday.setCreatedDate(new Date());
			holidaysRepo.save(holiday);			 
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
	
	@RequestMapping(value="/deleteHolidays")
	public boolean deleteHolidays(HttpServletRequest request, @RequestParam String username, @RequestBody List<XpdHoliday> holidays) {
		try {
			holidaysRepo.delete(holidays);
		} catch (Exception ex) {
			throw ex;
		}
		return true;
	}
	
	@RequestMapping(value="/getHolidayList")
	public List<XpdHoliday> getHolidayListForCompany(HttpServletRequest request, @RequestParam long companyId) {
		return holidaysRepo.getHolidaysForCompany(companyId);
	}
	
	@RequestMapping(value="/savePaymentDateStrategy")
	public boolean savePaymentDateStrategy(HttpServletRequest request,
											@RequestParam String username,	
											@RequestBody XpdPaymentDateStrategy paymentDateStrategy) {
		try {
			XpdPaymentDateStrategy newStrategy = paymentDateStrategy;
			XpdUser currentUser = this.userController.getUserByUsername(username);
			newStrategy.setCreatedBy(currentUser);
			newStrategy.setCreatedDate(new Date());
			newStrategy.setIsActive(true);
			if (paymentDateStrategy.getId() != null) {
				paymentStrategyRepo.inactiveCurrentPaymentStrategy(paymentDateStrategy.getCompany().getId());
			}
			newStrategy.setId(null);
			this.paymentStrategyRepo.save(newStrategy);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	@RequestMapping(value="/getPaymentStrategyForCompany")
	public XpdPaymentDateStrategy getPaymentStrategyForCompany(Long companyId) {
		return paymentStrategyRepo.findPaymentStrategyForCompany(companyId);
	}
	
}
