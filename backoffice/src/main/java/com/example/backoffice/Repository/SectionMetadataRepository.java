package com.example.backoffice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.SectionMetadata;

@Repository
public interface SectionMetadataRepository extends JpaRepository<SectionMetadata, String> {
}