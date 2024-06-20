package org.xpd.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdInvoice;
import org.xpd.models.dto.XpdInvoiceLight;

@Repository
public interface InvoiceRepo extends JpaRepository<XpdInvoice, Long> {

	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.currency, inv.xpdOrgMasterId.name) FROM XpdInvoice inv WHERE inv.xpdOrgMasterId.id in(?1)")
	List<XpdInvoiceLight> getCompanyInvoices(List<Long> orgMasterId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and inv.xpdCompanyId.id=?2 and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1','Approved','Posted on ERP') "
			+ " and status.isCurrent=1 and offer.isActive=true")
	List<XpdInvoiceLight> getSupplierUnderProcessingInvoices(List<Long> orgMasterIds, long xpdCompanyId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.currency, inv.xpdOrgMasterId.name) from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id\n" + 
			" AND inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Open') and status.isCurrent=1 and inv.dueDate > ?2")
	List<XpdInvoiceLight> getBuyerOpenInvoices(List<Long> orgMasterId, Date newDueDate);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id and offer.isActive=true " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1','Approved','Posted on ERP') and status.isCurrent=1")
	List<XpdInvoiceLight> getSupplierUnderProcessingInvoices(long companyId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name, status.createdBy.username) from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer WHERE inv.id = status.invoiceId.id" 
			+ " AND offer.isActive=true and offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1','Approved','Posted on ERP') and status.isCurrent=1")
	List<XpdInvoiceLight> getBuyerApprovalPendingInvoices(List<Long> orgMasterId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name, status.createdBy.username) from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer WHERE inv.id = status.invoiceId.id" 
			+ " AND offer.isActive=true and offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Approved','Posted on ERP') and status.isCurrent=1")
	List<XpdInvoiceLight> getBuyerUnderApprovalInvoices(List<Long> orgMasterId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id" + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Paid') "
			+ "and status.isCurrent=1 and offer.isActive=true "
			+ " and lower(inv.xpdCompanyId.companyName) like (lower(?2)) and inv.invoiceNumber like (?3)")
	List<XpdInvoiceLight> getBuyerPaidInvoices(List<Long> orgMasterId, String supplier, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Paid') "
			+ "and status.isCurrent=1 and offer.isActive=true "
			+ " and lower(inv.xpdCompanyId.companyName) like (lower(?2)) and status.createdDate >= ?3 and inv.invoiceNumber like (?4)")
	List<XpdInvoiceLight> getBuyerPaidInvoicesWithStartDate(List<Long> orgMasterId, String supplier, Date sDate, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Paid') "
			+ "and status.isCurrent=1 and offer.isActive=true "
			+ " and lower(inv.xpdCompanyId.companyName) like (lower(?2)) and status.createdDate <= ?3 and inv.invoiceNumber like (?4)")
	List<XpdInvoiceLight> getBuyerPaidInvoicesWithEndDate(List<Long> orgMasterId, String supplier, Date eDate, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Paid') "
			+ "and status.isCurrent=1 and offer.isActive=true "
			+ " and lower(inv.xpdCompanyId.companyName) like (lower(?2)) and status.createdDate >= ?3"
			+ " and status.createdDate <= ?4 and inv.invoiceNumber like (?5)")
	List<XpdInvoiceLight> getBuyerPaidInvoicesWithStartEndDate(List<Long> orgMasterId, String supplier, Date sDate, Date eDate, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id" + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdOrgMasterId.id in (?1) and status.isCurrent=1"
			+ " and offer.createdDate >= ?2 and offer.createdDate <= ?3")
	List<XpdInvoiceLight> getBuyerInvoicesSummaryWithStartEndDate(List<Long> orgMasterId, Date sDate, Date eDate);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and status.statusId.xpdDesc in ('Paid') and status.isCurrent=1")
	List<XpdInvoiceLight> getSupplierPaidInvoices(long xpdCompanyId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and inv.xpdOrgMasterId.id in (?2) "
			+ "and status.statusId.xpdDesc in ('Paid') and status.isCurrent=1 and offer.isActive=true "
			+ "and status.createdDate >= ?3 and status.createdDate <= ?4 and inv.invoiceNumber like (?5)")
	List<XpdInvoiceLight> getSupplierPaidInvoices(long xpdCompanyId, List<Long> locations, Date fromDate, Date toDate, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and inv.xpdOrgMasterId.id in (?2) "
			+ "and status.statusId.xpdDesc in ('Paid') and status.isCurrent=1 and offer.isActive=true and inv.invoiceNumber like (?3)")
	List<XpdInvoiceLight> getSupplierPaidInvoicesWithoutStartEndDates(long xpdCompanyId, List<Long> locations, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and inv.xpdOrgMasterId.id in (?2) "
			+ "and status.statusId.xpdDesc in ('Paid') and status.isCurrent=1 and offer.isActive=true "
			+ " and status.createdDate <= ?3 and inv.invoiceNumber like (?4)")
	List<XpdInvoiceLight> getSupplierPaidInvoicesWithoutFromDate(long xpdCompanyId, List<Long> locations, Date toDate, String invoiceNumber);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, status.createdDate, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id " + 
			" AND offer.xpdInvoiceId.id = inv.id and inv.xpdCompanyId.id=?1 and inv.xpdOrgMasterId.id in (?2) "
			+ "and status.statusId.xpdDesc in ('Paid') and status.isCurrent=1 and offer.isActive=true "
			+ "and status.createdDate >= ?3 and inv.invoiceNumber like (?4)")
	List<XpdInvoiceLight> getSupplierPaidInvoicesWithoutToDate(long xpdCompanyId, List<Long> locations, Date fromDate, String invoiceNumber);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency, inv.xpdOrgMasterId.name) FROM XpdInvoice inv "
			+ "WHERE xpdOrgMasterId.id in (?1) and dueDate > ?2 and inv.xpdCompanyId.id=?3 and processed=false order by xpdOrgMasterId.name")
	List<XpdInvoiceLight> getLocationInvoicesWithDateFilter(List<Long> orgMasterIds, Date newPaymentDate, long xpdCompanyId);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency, inv.xpdOrgMasterId.name) FROM XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id "
			+ "and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Open') and status.isCurrent=1 and inv.dueDate > ?2 order by inv.xpdOrgMasterId.name")
	List<XpdInvoiceLight> getLocationOpenInvoicesWithDateFilter(List<Long> orgMasterIds, Date newPaymentDate);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency,"
			+"status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining,"
			+"offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "FROM XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id and offer.xpdInvoiceId.id=inv.id and offer.isActive=true "
			+ "and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Rejected') and status.isCurrent=1 and inv.dueDate > ?2 order by inv.xpdOrgMasterId.name")
	List<XpdInvoiceLight> getLocationRejectedInvoicesWithDateFilter(List<Long> orgMasterIds, Date newPaymentDate);
	
//	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, \"\n" + 
//			"			+ \"inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, offer.discountPercent30Days, status.statusId.buyerDesc, inv.currency) \"\n" + 
//			"			+ \"from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer WHERE inv.id = status.invoiceId.id "
//			+ " and offer.xpdInvoiceId.id=inv.id and inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Rejected') and status.isCurrent=1 and inv.dueDate > ?2 order by xpdOrgMasterId.name")
//	List<XpdInvoiceLight> getLocationRejectedInvoicesWithDateFilter(List<Long> orgMasterIds, Date newPaymentDate);
	
	@Query("Select distinct xpdOrgMasterId.id FROM XpdInvoice inv WHERE xpdCompanyId.id=?1")
	List<Long> getAvailableInvoiceLocations(long xpdCompanyId);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdOrgMasterId.name) FROM XpdInvoice inv WHERE xpdCompanyId.id=?1")
	List<XpdInvoiceLight> getAllCompanyInvoices(long xpdCompanyId);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency, inv.xpdOrgMasterId.name) from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id \n" + 
			"AND inv.xpdCompanyId.id=?2 and status.statusId.xpdDesc in ('Open') and status.isCurrent=1 and inv.dueDate > ?3 and inv.xpdOrgMasterId.id in (?1)")
	List<XpdInvoiceLight> getAllCompanyInvoicesWithDateFilter(List<Long> orgMasterIds, long xpdCompanyId, Date newDueDate);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, inv.xpdCompanyId.companyName, inv.currency," 
			+ "status.statusId.supplierDesc, status.statusId.buyerDesc, status.statusId.xpdDesc, offer.annualPercentage, offer.discountAmount, offer.daysRemaining," 
			+ "offer.discountPercent30Days, offer.appliedDiscount, offer.netAmount, offer.newDueDate, offer.dueAmount, offer.createdDate, inv.xpdOrgMasterId.name) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id and offer.xpdInvoiceId.id=inv.id \n" + 
			"AND inv.xpdCompanyId.id=?2 and status.statusId.xpdDesc in ('Rejected') and status.isCurrent=1 and inv.dueDate > ?3 and offer.isActive=true"
			+ " and inv.xpdOrgMasterId.id in (?1)")
	List<XpdInvoiceLight> getAllCompanyRejectedInvoicesWithDateFilter(List<Long> orgMasterIds, long xpdCompanyId, Date newDueDate);
	
	@Query("SELECT new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, "
			+ "inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate, offer.discountPercent30Days, status.statusId.supplierDesc, inv.currency) "
			+ "from XpdInvoice inv, XpdInvoiceStatus status, XpdInvoiceOffer offer "
			+ "WHERE inv.id = status.invoiceId.id and offer.xpdInvoiceId.id=inv.id \n" + 
			"AND inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Rejected') and status.isCurrent=1 and inv.dueDate > ?2")
	List<XpdInvoiceLight> getAllCompanyRejectedInvoicesWithDateFilter(List<Long> xpdOrgMasterIds, Date newDueDate);
	
	@Query("select sum(inv.totalTrasactionValue), count(inv.totalTrasactionValue) from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id\n" + 
			" AND inv.xpdCompanyId.id=?1 and status.statusId.xpdDesc in ('Open', 'Rejected') and status.isCurrent=1 and inv.dueDate > ?2")
	Object getSupplierUnderOfferDashboardData(long xpdCompanyId, Date newPaymentDate);
	
	@Query("select sum(inv.totalTrasactionValue), count(inv.totalTrasactionValue) from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id\n" + 
		" AND inv.xpdCompanyId.id=?1 and status.statusId.xpdDesc in ('Open', 'Rejected') and status.isCurrent=1 and inv.dueDate > ?2 and inv.xpdOrgMasterId.id in (?3)")
	Object getSupplierDashboardDataWithBuyerLocations(long xpdCompanyId, Date newPaymentDate, List<Long> orgMasterIds);
	
	@Query("select sum(inv1.totalTrasactionValue), count(inv1.totalTrasactionValue) from XpdInvoice inv1, XpdInvoiceStatus status WHERE inv1.id = status.invoiceId.id\n" + 
			" 	AND inv1.xpdCompanyId.id=?1 and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1','Approved','Posted on ERP') and status.isCurrent=1")
	Object getSupplierUnderProcessingDashboardData(long xpdCompanyId);
	
	@Query("select sum(inv.totalTrasactionValue), avg(offer.daysRemaining) from XpdInvoice inv, XpdInvoiceStatus invstatus, XpdInvoiceOffer offer\n" + 
			" where inv.id=invstatus.invoiceId.id and inv.id=offer.xpdInvoiceId.id and invstatus.statusId.xpdDesc = 'Paid' and inv.xpdCompanyId.id=?1")
	Object getSupplierCompletedDashboardData(long xpdCompanyId);

	@Query("select sum(inv.totalTrasactionValue) from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id\n" + 
			" AND inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Open', 'Rejected') and status.isCurrent=1 and inv.dueDate > ?2")
	Object getBuyerUnderOfferDashboardData(List<Long> xpdOrgMasterIds, Date nextDueDate);
	
	@Query("select new org.xpd.models.dto.XpdInvoiceLight(inv.id, inv.invoiceNumber, inv.invoiceDate, inv.totalTrasactionValue, inv.dueDate)  from XpdInvoice inv, XpdInvoiceStatus status WHERE inv.id = status.invoiceId.id\n" + 
			" AND inv.xpdOrgMasterId.id in (?1) and status.statusId.xpdDesc in ('Open', 'Rejected') and status.isCurrent=1 and inv.dueDate > ?2")
	List<XpdInvoiceLight> getBuyerUnderOfferDaysDashboardData(List<Long> xpdOrgMasterIds, Date newPaymentDate);
	
	
	@Query("select sum(inv.totalTrasactionValue) from XpdInvoice inv, XpdInvoiceStatus invstatus \n" + 
			" where inv.id=invstatus.invoiceId.id and invstatus.statusId.xpdDesc in('Verified by Checker','Requested by Supplier','Approved by Level 1') and inv.xpdOrgMasterId.id in(?1) and invstatus.isCurrent=1")
	Object getBuyerUnderApprovalDashboardData(List<Long> xpdOrgMasterIds);
	
	@Query("select sum(inv.totalTrasactionValue) from XpdInvoice inv, XpdInvoiceStatus invstatus \n" + 
			" where inv.id=invstatus.invoiceId.id and invstatus.statusId.xpdDesc in('Approved','Posted on ERP') and inv.xpdOrgMasterId.id in(?1) and invstatus.isCurrent=1")
	Object getBuyerUnderClearingDashboardData(List<Long> xpdOrgMasterIds);
	
	@Query("select sum(offer.discountAmount) from XpdInvoice inv, XpdInvoiceStatus invstatus, XpdInvoiceOffer offer \n" + 
			" where inv.id=invstatus.invoiceId.id and inv.id=offer.xpdInvoiceId.id and invstatus.statusId.xpdDesc in('Paid') and inv.xpdOrgMasterId.id in(?1) "
			+ "and invstatus.isCurrent=1 and offer.isActive=true")
	Object getBuyerPaidDashboardData(List<Long> xpdOrgMasterIds);
	
	@Query("select avg(offer.annualPercentage) from XpdInvoice inv, XpdInvoiceStatus invstatus, XpdInvoiceOffer offer \n" + 
			" where inv.id=invstatus.invoiceId.id and inv.id=offer.xpdInvoiceId.id and invstatus.statusId.xpdDesc in('Paid') and inv.xpdOrgMasterId.id in(?1) "
			+ "and invstatus.isCurrent=1 and offer.isActive=true")
	Object getBuyerPaidAPRDashboardData(List<Long> xpdOrgMasterIds);
	

	
}
