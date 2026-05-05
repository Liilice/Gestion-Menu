package com.example.backoffice.Repository;

import org.springframework.stereotype.Repository;

import com.example.backoffice.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}