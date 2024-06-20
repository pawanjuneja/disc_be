package org.xpd.models;
// Generated 20 Aug, 2018 7:46:07 PM by Hibernate Tools 5.3.1.Final

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Lazy;

/**
 * Xpdcompany generated by hbm2java
 */
@Entity
@Table
public class XpdCompanyAddress implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdCompany xpdCompanyId;
	
	@ManyToOne
	private SysAddressType sysAddressTypeId;
	
	@Column
	private String addressLine1;
	
	@Column
	private String addressLine2;
	
	@Column
	private String addressLine3;
	
	@Column
	private String addressLine4;
	
	@Column
	private String country;
	
	@Column
	private String state;
	
	@Column
	private String city;
	
	@Column
	private String pincode;
	
	@Column
	private XpdUser createdby;
	
	@Column
	private String createddate;
	
	@Column
	private XpdUser modifiedby;
	
	@Column
	private String modifieddate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public XpdCompany getXpdCompanyId() {
		return xpdCompanyId;
	}

	public void setXpdCompanyId(XpdCompany xpdCompanyId) {
		this.xpdCompanyId = xpdCompanyId;
	}

	public SysAddressType getSysAddressTypeId() {
		return sysAddressTypeId;
	}

	public void setSysAddressTypeId(SysAddressType sysAddressTypeId) {
		this.sysAddressTypeId = sysAddressTypeId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public XpdUser getCreatedby() {
		return createdby;
	}

	public void setCreatedby(XpdUser createdby) {
		this.createdby = createdby;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public XpdUser getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(XpdUser modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(String modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	
}
