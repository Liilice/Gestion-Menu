package com.example.backoffice.Exception.notFoundException;

public class DishRelationNotFoundException extends NotFoundException {
    public DishRelationNotFoundException(String id) {
        super("DishRelation not found with id: " + id);
    }
}
