package org.xpd.models.dto;

import java.io.Serializable;

import org.xpd.models.XpdCompany;
import org.xpd.models.XpdPaymentDateStrategy;

public class PaymentStrategyDto implements Serializable {

	private XpdCompany company;
	
	private XpdPaymentDateStrategy paymentStrategy;

	public XpdCompany getCompany() {
		return company;
	}

	public void setCompany(XpdCompany company) {
		this.company = company;
	}

	public XpdPaymentDateStrategy getPaymentStrategy() {
		return paymentStrategy;
	}

	public void setPaymentStrategy(XpdPaymentDateStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}
}
