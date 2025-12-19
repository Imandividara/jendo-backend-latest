package com.jendo.app.domain.reportsection.entity;

import com.jendo.app.domain.reportcategory.entity.ReportCategory;
import com.jendo.app.domain.reportitem.entity.ReportItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"reportCategory", "reportItems"})
@ToString(exclude = {"reportCategory", "reportItems"})
public class ReportSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ReportCategory reportCategory;

    @OneToMany(mappedBy = "reportSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportItem> reportItems = new ArrayList<>();
}
