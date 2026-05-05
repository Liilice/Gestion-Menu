package com.example.backoffice.Exception.notFoundException;

public class MenuMetadataNotFoundException extends NotFoundException {
    public MenuMetadataNotFoundException(String id) {
        super("Menu metadata with id " + id + " not found.");
    }
    
}
