package com.example.backoffice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {
}
