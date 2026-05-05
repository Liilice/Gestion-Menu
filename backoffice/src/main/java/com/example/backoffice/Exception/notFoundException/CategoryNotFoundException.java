package com.example.backoffice.Exception.notFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String id) {
        super("Category not found with id: " + id);
    }
}