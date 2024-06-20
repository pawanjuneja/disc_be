package org.xpd.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table
public class XpdHoliday implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdCompany company;
	
	@ManyToMany
	private List<XpdOrgMaster> locations;
	
	private String holidayDescription;
	private Date holidayDate;
	private String holidayId;
	
	@ManyToOne
	private XpdUser createdBy;
	
	private Date createdDate;
	
	@ManyToOne
	private XpdUser modifiedBy;
	
	private Date modifiedDate;
	
	private boolean isNationalHoliday;
	
	public XpdHoliday() {
		
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

	public List<XpdOrgMaster> getLocations() {
		return locations;
	}

	public void setLocations(List<XpdOrgMaster> locations) {
		this.locations = locations;
	}
}
