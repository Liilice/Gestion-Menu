package com.example.backoffice.Exception.notFoundException;

public class MenuRelationNotFoundException extends NotFoundException {
    public MenuRelationNotFoundException(String id) {
        super("Menu relation not found with id: " + id);
    }
    
}
