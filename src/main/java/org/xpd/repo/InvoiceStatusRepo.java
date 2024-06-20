package org.xpd.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xpd.models.XpdInvoiceStatus;
import org.xpd.models.XpdStatus;
import org.xpd.models.dto.XpdInvoiceStatusLight;

@Repository
public interface InvoiceStatusRepo extends JpaRepository<XpdInvoiceStatus, Long> {

	@Query("From XpdStatus s where s.workflowSequence=?1")
	XpdStatus getWorkflowStatusBySequenceId(int sequenceId);
	
	@Query("From XpdStatus s where s.xpdDesc=?1")
	XpdStatus getWorkflowStatusByStatusText(String statusText);
	
	@Query("From XpdInvoiceStatus status where status.invoiceId.id=?1 order by id desc")
	List<XpdInvoiceStatus> getCurrentInvoiceStatus(long invoiceId);
	
	@Query("Select new org.xpd.models.dto.XpdInvoiceStatusLight(status.id, status.statusId.workflowSequence, status.invoiceId.id) From XpdInvoiceStatus status "
			+ "where status.isCurrent=1 and status.invoiceId.xpdOrgMasterId.id in (?1)")
	List<XpdInvoiceStatusLight> getAllActiveInvoiceStatuses(List<Long> companyLocationIds);
	
	@Query("From XpdInvoiceStatus status where status.invoiceId.id=?1 and isCurrent=1")
	XpdInvoiceStatus getActiveInvoiceStatus(long invoiceId);
	
	@Query("From XpdInvoiceStatus status where status.invoiceId.id=?1 and statusId.workflowSequence=2")
	XpdInvoiceStatus getSubmittedInvoiceStatus(long invoiceId);
	
	@Transactional
	@Modifying (clearAutomatically=true)
	@Query("Update XpdInvoiceStatus set isCurrent=0 where invoiceId.id in (:invoiceIds)")
	void inactiveCurrentInvoiceStatus(@Param("invoiceIds") List<Long> invoiceIds);
	
}
