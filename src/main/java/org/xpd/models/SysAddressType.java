package org.xpd.models;
// Generated 20 Aug, 2018 7:46:07 PM by Hibernate Tools 5.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table
public class SysAddressType implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private String addressType;
	
	@Column
	private String description;
	
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

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
