package com.jendo.app.domain.learningmaterial.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_materials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "author", length = 200)
    private String author;

    @Column(name = "duration", length = 50)
    private String duration;

    @Column(name = "description")
    private String description;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "category", length = 100)
    private String category;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
