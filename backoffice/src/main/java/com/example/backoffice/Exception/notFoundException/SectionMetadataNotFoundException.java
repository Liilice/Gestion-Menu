package com.example.backoffice.Exception.notFoundException;

public class SectionMetadataNotFoundException extends NotFoundException {
    public SectionMetadataNotFoundException(String id) {
        super("Section metadata not found with id: " + id);
    }
}
