package com.example.backoffice.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

@Entity
public class SectionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "sectionMetadada_id", nullable = false)
    private SectionMetadata sectionMetadata;

    @ManyToOne()
    @JoinColumn(name = "dishMetadata_id", nullable = false)
    private DishMetadata dishMetadata;

    @CreationTimestamp()
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SectionMetadata getSectionMetadata() {
        return sectionMetadata;
    }

    public void setSectionMetadata(SectionMetadata sectionMetadata) {
        this.sectionMetadata = sectionMetadata;
    }

    public DishMetadata getDishMetadata() {
        return dishMetadata;
    }

    public void setDishMetadata(DishMetadata dishMetadata) {
        this.dishMetadata = dishMetadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
