package com.jendo.app.domain.reportitemvalue.repository;

import com.jendo.app.domain.reportitemvalue.entity.ReportItemValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportItemValueRepository extends JpaRepository<ReportItemValue, Long> {
    
    List<ReportItemValue> findByReportItemId(Long reportItemId);

    @Query("SELECT v FROM ReportItemValue v WHERE v.user.id = :userId")
    List<ReportItemValue> findByUserId(@Param("userId") Long userId);

    @Query("SELECT v FROM ReportItemValue v WHERE v.user.id = :userId AND v.reportItem.id = :reportItemId")
    List<ReportItemValue> findByUserIdAndReportItemId(@Param("userId") Long userId, @Param("reportItemId") Long reportItemId);
}
