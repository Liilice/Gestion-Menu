package com.example.backoffice.DTO.category;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {
    @NotBlank(message = "Name field is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}