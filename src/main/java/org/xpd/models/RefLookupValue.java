package org.xpd.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class RefLookupValue implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@ManyToOne
	private RefLookupType refLookupType;
	@Column
	private String refLookupValue;
	@Column
	private long createdBy;
	@Column
	private Date createdDate;
	@Column
	private String description;
	@Column
	private long modifiedBy;
	@Column
	private Date modifiedDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public RefLookupType getRefLookupTypeId() {
		return refLookupType;
	}
	public void setRefLookupTypeId(RefLookupType refLookupType) {
		this.refLookupType = refLookupType;
	}
	public String getRefLookupValue() {
		return refLookupValue;
	}
	public void setRefLookupValue(String refLookupValue) {
		this.refLookupValue = refLookupValue;
	}
	public long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
}
