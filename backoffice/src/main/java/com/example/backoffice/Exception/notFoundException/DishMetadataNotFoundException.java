package com.example.backoffice.Exception.notFoundException;

public class DishMetadataNotFoundException extends NotFoundException {
    public DishMetadataNotFoundException(String id) {
        super("DishMetadata not found with id: " + id);
    }

}
