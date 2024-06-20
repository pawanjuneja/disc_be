package org.xpd.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xpd.models.XpdInvoiceNote;
import org.xpd.models.dto.XpdInvoiceNoteLight;

@Repository
public interface InvoiceNotesRepo extends JpaRepository<XpdInvoiceNote, Long> {

	@Query("Select new org.xpd.models.dto.XpdInvoiceNoteLight(note.totalTrasactionValue, note.noteType.noteDesc, note.createdDate) FROM XpdInvoiceNote note where xpdInvoiceId.id=?1 and noteReason not like(?2)")
	public List<XpdInvoiceNoteLight> getInvoiceNotes(long invoiceId, String noteReason);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceNoteLight(note.totalTrasactionValue, note.noteType.noteDesc, note.createdDate, note.xpdInvoiceId.id) FROM XpdInvoiceNote note where noteReason not like(?1)")
	public List<XpdInvoiceNoteLight> findAllNotes(String noteReason);
	
	@Query("Select sum(note.totalTrasactionValue) FROM XpdInvoiceNote note, XpdInvoiceStatus status where note.noteReason not like(?1) and note.noteType.noteDesc=?2"
			+ " and note.xpdInvoiceId.id=status.invoiceId.id and status.statusId.xpdDesc in ('Open','Rejected') and status.isCurrent=1"
			+ " and note.xpdInvoiceId.dueDate > ?3 and note.xpdInvoiceId.xpdOrgMasterId.id in (?4)")
	public Double getSumOfNotesValueForPaymentsDueInvoices(String noteReason, String noteDesc, Date nextDueDate, List<Long> orgMasterIds);
	
	@Query("Select sum(note.totalTrasactionValue) FROM XpdInvoiceNote note, XpdInvoiceStatus status where note.noteReason not like(?1) and note.noteType.noteDesc=?2"
			+ " and note.xpdInvoiceId.id=status.invoiceId.id and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1')"
			+ " and  status.isCurrent=1 and note.xpdInvoiceId.xpdOrgMasterId.id in (?3)")
	public Double getSumOfNotesValueForUnderApprovalInvoices(String noteReason, String noteDesc, List<Long> orgMasterIds);
	
	@Query("Select sum(note.totalTrasactionValue) FROM XpdInvoiceNote note, XpdInvoiceStatus status where note.noteReason not like(?1) and note.noteType.noteDesc=?2"
			+ " and note.xpdInvoiceId.id=status.invoiceId.id and status.statusId.xpdDesc in ('Approved','Posted on ERP')"
			+ " and status.isCurrent=1 and note.xpdInvoiceId.xpdOrgMasterId.id in (?3)")
	public Double getSumOfNotesValueForUnderClearingInvoices(String noteReason, String noteDesc, List<Long> orgMasterIds);
	
	@Query("Select sum(note.totalTrasactionValue) FROM XpdInvoiceNote note, XpdInvoiceStatus status where note.noteReason not like(?1) and note.noteType.noteDesc=?2"
			+ " and note.xpdInvoiceId.id=status.invoiceId.id and status.statusId.xpdDesc in ('Open','Rejected') and status.isCurrent=1"
			+ " and note.xpdInvoiceId.dueDate > ?3 and note.xpdInvoiceId.xpdCompanyId.id=?4 and note.xpdInvoiceId.xpdOrgMasterId.id in (?5)")
	public Double getSumOfNotesValueForPaymentsDueSupplierInvoices(String noteReason, String noteDesc, Date nextDueDate, Long xpdCompanyId, List<Long> orgMasterIds);
	
	@Query("Select sum(note.totalTrasactionValue) FROM XpdInvoiceNote note, XpdInvoiceStatus status where note.noteReason not like(?1) and note.noteType.noteDesc=?2"
			+ " and note.xpdInvoiceId.id=status.invoiceId.id and status.statusId.xpdDesc in ('Verified by Checker','Requested by Supplier','Approved by Level 1','Approved','Posted on ERP')"
			+ " and  status.isCurrent=1 and note.xpdInvoiceId.xpdCompanyId.id=?3")
	public Double getSumOfNotesValueForUnderApprovalSupplierInvoices(String noteReason, String noteDesc, Long xpdCompanyId);
	
	
	@Query("FROM XpdInvoiceNote where xpdInvoiceId.id=?1")
	public List<XpdInvoiceNoteLight> getInvoiceNotes(long invoiceId);
}
