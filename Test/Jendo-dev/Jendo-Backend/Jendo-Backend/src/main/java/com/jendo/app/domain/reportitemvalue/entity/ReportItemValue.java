package com.jendo.app.domain.reportitemvalue.entity;

import com.jendo.app.domain.reportitem.entity.ReportItem;
import com.jendo.app.domain.reportattachment.entity.ReportAttachment;
import com.jendo.app.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_item_values")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"reportItem", "reportAttachments", "user"})
@ToString(exclude = {"reportItem", "reportAttachments", "user"})
public class ReportItemValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value_number", precision = 10, scale = 2)
    private BigDecimal valueNumber;

    @Column(name = "value_text")
    private String valueText;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_item_id", nullable = false)
    private ReportItem reportItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reportItemValue", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportAttachment> reportAttachments = new ArrayList<>();
}
