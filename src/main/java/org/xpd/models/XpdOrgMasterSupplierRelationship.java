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
 * Xpdorgmastersupplierrelationship generated by hbm2java
 */
@Entity
@Table
public class XpdOrgMasterSupplierRelationship implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdCompanyRelationship xpdCompanyRelationshipId;
	
	@ManyToOne
	private XpdOrgMaster xpdOrgMasterId;
	
	@Column
	private long createdBy;
	@Column
	private Date createdDate;
	@Column
	private long modifiedBy;
	@Column
	private Date modifiedDate;
	@Column
	private Boolean isActive;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public XpdCompanyRelationship getXpdCompanyRelationshipId() {
		return xpdCompanyRelationshipId;
	}

	public void setXpdCompanyRelationshipId(XpdCompanyRelationship xpdCompanyRelationshipId) {
		this.xpdCompanyRelationshipId = xpdCompanyRelationshipId;
	}

	public XpdOrgMaster getXpdOrgMasterId() {
		return xpdOrgMasterId;
	}

	public void setXpdOrgMasterId(XpdOrgMaster xpdOrgMasterId) {
		this.xpdOrgMasterId = xpdOrgMasterId;
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

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
