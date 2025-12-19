package com.jendo.app.domain.reportitem.entity;

import com.jendo.app.domain.reportsection.entity.ReportSection;
import com.jendo.app.domain.reportitemvalue.entity.ReportItemValue;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"reportSection", "reportItemValues"})
@ToString(exclude = {"reportSection", "reportItemValues"})
public class ReportItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "icon", length = 100)
    private String icon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_section_id")
    private ReportSection reportSection;

    @OneToMany(mappedBy = "reportItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportItemValue> reportItemValues = new ArrayList<>();
}
