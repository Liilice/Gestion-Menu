package com.example.backoffice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.SectionRelation;

@Repository
public interface SectionRelationRepository extends JpaRepository<SectionRelation, String> {
}