package com.example.backoffice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.DishMetadata;

@Repository
public interface DishMetadataRepository extends JpaRepository<DishMetadata, String> {

}