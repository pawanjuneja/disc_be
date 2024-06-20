package org.xpd.models.dto;

import java.util.List;

import org.xpd.models.XpdCompany;

public class MoreCompanies implements java.io.Serializable {

	List<XpdCompany> lstSupplierCompanies;
	
	List<XpdCompany> lstBuyerCompanies;

	public List<XpdCompany> getLstSupplierCompanies() {
		return lstSupplierCompanies;
	}

	public void setLstSupplierCompanies(List<XpdCompany> lstSupplierCompanies) {
		this.lstSupplierCompanies = lstSupplierCompanies;
	}

	public List<XpdCompany> getLstBuyerCompanies() {
		return lstBuyerCompanies;
	}

	public void setLstBuyerCompanies(List<XpdCompany> lstBuyerCompanies) {
		this.lstBuyerCompanies = lstBuyerCompanies;
	}
	
	
}
