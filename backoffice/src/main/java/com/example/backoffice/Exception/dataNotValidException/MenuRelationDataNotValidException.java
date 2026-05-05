package com.example.backoffice.Exception.dataNotValidException;

public class MenuRelationDataNotValidException extends DataNotValidException {
    public MenuRelationDataNotValidException() {
        super("DishMetadataId or SectionMetadataId is required");
    }
}