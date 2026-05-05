package com.example.backoffice.DTO.dishMetadata;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class DishMetadataDTO {
    @NotBlank(message = "Name field is required")
    private String name;

    private String description;

    @NotNull(message = "Price field is required")
    @Positive(message = "Price must be a positive value")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
