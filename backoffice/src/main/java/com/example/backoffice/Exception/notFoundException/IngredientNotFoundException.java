package com.example.backoffice.Exception.notFoundException;

public class IngredientNotFoundException extends NotFoundException {
    public IngredientNotFoundException(String id) {
        super("Ingredient not found with id: " + id);
    }
}