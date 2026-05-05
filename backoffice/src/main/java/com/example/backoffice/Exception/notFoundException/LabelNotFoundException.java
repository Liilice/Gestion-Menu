package com.example.backoffice.Exception.notFoundException;

public class LabelNotFoundException extends NotFoundException {
    public LabelNotFoundException(String id) {
        super("Label not found with id: " + id);
    }

}
