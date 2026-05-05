package com.example.backoffice.Exception.notFoundException;

public class SectionRelationNotFoundException extends NotFoundException {
    public SectionRelationNotFoundException(String id) {
        super("SectionRelation with id " + id + " not found");
    }
    
}
