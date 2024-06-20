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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Xpdorgmasterlevel generated by hbm2java
 */
@Entity
@Table
public class XpdOrgMasterLevel implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private XpdCompany xpdCompanyId;

	@ManyToOne
	private XpdOrgMasterLevel xpdOrgMasterLevel;
	
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private long createdBy;
	@Column
	private Date createdDate;
	@Column
	private long modifiedBy;
	@Column
	private Date modifiedDate;

	public long getId() {
		return this.id;
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

	public XpdOrgMasterLevel getXpdOrgMasterLevel() {
		return xpdOrgMasterLevel;
	}

	public void setXpdOrgMasterLevel(XpdOrgMasterLevel xpdOrgMasterLevel) {
		this.xpdOrgMasterLevel = xpdOrgMasterLevel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}