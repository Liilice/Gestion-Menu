package com.example.backoffice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.DishRelation;

@Repository
public interface DishRelationRepository extends JpaRepository<DishRelation, String> {

}