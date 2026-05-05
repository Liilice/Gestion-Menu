package com.example.backoffice.Exception.dataNotValidException;

public class DishRelationDataNotValidException extends DataNotValidException {
    public DishRelationDataNotValidException() {
        super("IngredientId or labelId is required");
    }
    
}
