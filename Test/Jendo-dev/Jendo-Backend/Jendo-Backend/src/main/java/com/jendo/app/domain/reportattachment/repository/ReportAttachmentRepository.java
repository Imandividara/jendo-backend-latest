package com.jendo.app.domain.reportattachment.repository;

import com.jendo.app.domain.reportattachment.entity.ReportAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAttachmentRepository extends JpaRepository<ReportAttachment, Long> {
    
    List<ReportAttachment> findByReportItemValueId(Long reportItemValueId);
}
