package org.xpd.models;
// Generated 20 Aug, 2018 7:46:07 PM by Hibernate Tools 5.3.1.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 */
@Entity
@Table
public class XpdNewPaymentDates implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column
	private Date curentDate;
	@Column
	private Date nextPaymentDate;
	
	@ManyToOne
	private XpdCompany buyerCompany;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCurrentDate() {
		return curentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.curentDate = currentDate;
	}

	public Date getNextPaymentDate() {
		return nextPaymentDate;
	}

	public void setNextPaymentDate(Date nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public XpdCompany getBuyerCompany() {
		return buyerCompany;
	}

	public void setBuyerCompany(XpdCompany buyerCompany) {
		this.buyerCompany = buyerCompany;
	}
}
