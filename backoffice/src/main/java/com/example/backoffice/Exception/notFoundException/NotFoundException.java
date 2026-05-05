package com.example.backoffice.Exception.notFoundException;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    
}
