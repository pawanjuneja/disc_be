package org.xpd.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table
public class XpdUser implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private XpdCompany xpdcompany;
	
	@ManyToMany
	private List<XpdCompany> linkedCompany;
	
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private boolean isApprover;
	@Column
	private boolean isChecker;
	@Column
	private boolean isAdmin;
	@Column
	private boolean isXpdUser;
	@Column
	private boolean isActive;
	@Column
	private long createdBy;
	@Column
	private Date createdDate;
	@Column
	private long modifiedBy;
	@Column
	private Date modifiedDate;
	
	@Column(nullable = true)
	private boolean isTNC = false;
	
	@Column(nullable = true)
	private boolean isBuyer = false;
	
	public boolean getIsBuyer() {
		return isBuyer;
	}
	public void setIsBuyer(boolean isBuyer) {
		this.isBuyer = isBuyer;
	}
	private String mobileNumber;
	
	private String arn;
	
	public boolean isTNC() {
		return isTNC;
	}
	public void setTNC(boolean isTNC) {
		this.isTNC = isTNC;
	}
	public String getArn() {
		return arn;
	}
	public void setArn(String arn) {
		this.arn = arn;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public XpdCompany getXpdcompany() {
		return xpdcompany;
	}
	public void setXpdcompany(XpdCompany xpdcompany) {
		this.xpdcompany = xpdcompany;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public boolean getIsApprover() {
		return isApprover;
	}
	public void setIsApprover(boolean isApprover) {
		this.isApprover = isApprover;
	}
	public boolean getIsChecker() {
		return isChecker;
	}
	public void setIsChecker(boolean isChecker) {
		this.isChecker = isChecker;
	}
	public List<XpdCompany> getLinkedCompany() {
		return linkedCompany;
	}
	public void setLinkedCompany(List<XpdCompany> linkedCompany) {
		this.linkedCompany = linkedCompany;
	}
	public boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public boolean getIsXpdUser() {
		return isXpdUser;
	}
	public void setIsXpdUser(boolean isXpdUser) {
		this.isXpdUser = isXpdUser;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
