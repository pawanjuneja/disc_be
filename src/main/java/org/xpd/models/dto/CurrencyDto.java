package org.xpd.models.dto;

import java.util.Date;

import javax.persistence.Column;

public class CurrencyDto implements java.io.Serializable {

	private String currency;
	private String symbol;
	
	public CurrencyDto(String currency, String symbol) {
		this.currency = currency;
		this.symbol = symbol;
	}
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	
}
