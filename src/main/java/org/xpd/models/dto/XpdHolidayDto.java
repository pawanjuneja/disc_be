package org.xpd.models.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xpd.models.XpdCompany;
import org.xpd.models.XpdOrgMaster;
import org.xpd.models.XpdUser;

public class XpdHolidayDto implements java.io.Serializable {

	private long id;
	private XpdCompany company;
	private XpdOrgMaster location;
	
	private String holidayDescription;
	private Date holidayDate;
	private String holidayId;
	
	private XpdUser createdBy;
	
	private Date createdDate;
	
	private XpdUser modifiedBy;
	
	private Date modifiedDate;
	
	private boolean isNationalHoliday;
	
	private List<XpdOrgMaster> orgMasters = new ArrayList<XpdOrgMaster>();
	
	public XpdHolidayDto() {
		
	}
	
	public XpdHolidayDto(XpdCompany company, XpdOrgMaster location, String holidayDescription, Date holidayDate,
			XpdUser createdBy, Date createdDate, boolean isNationalHoliday) {
		super();
		this.company = company;
		this.location = location;
		this.holidayDescription = holidayDescription;
		this.holidayDate = holidayDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.isNationalHoliday = isNationalHoliday;
	}



	public XpdHolidayDto(String holidayId, Long id, String holidayDescription, Date holidayDate,
			XpdCompany company, boolean isNationalHoliday) {
		super();
		this.id = id;
		this.company = company;
		this.location = location;
		this.holidayDescription = holidayDescription;
		this.holidayDate = holidayDate;
		this.holidayId = holidayId;
		this.isNationalHoliday = isNationalHoliday;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public XpdCompany getCompany() {
		return company;
	}
	public void setCompany(XpdCompany company) {
		this.company = company;
	}
	public XpdOrgMaster getLocation() {
		return location;
	}
	public void setLocation(XpdOrgMaster location) {
		this.location = location;
	}
	public String getHolidayDescription() {
		return holidayDescription;
	}
	public void setHolidayDescription(String holidayDescription) {
		this.holidayDescription = holidayDescription;
	}
	public boolean getIsNationalHoliday() {
		return isNationalHoliday;
	}
	public void setIsNationalHoliday(boolean isNationalHoliday) {
		this.isNationalHoliday = isNationalHoliday;
	}
	
	public String getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}
	public XpdUser getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(XpdUser createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public XpdUser getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(XpdUser modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Date getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public List<XpdOrgMaster> getOrgMasters() {
		return orgMasters;
	}

	public void setOrgMasters(List<XpdOrgMaster> orgMasters) {
		this.orgMasters = orgMasters;
	}
}
