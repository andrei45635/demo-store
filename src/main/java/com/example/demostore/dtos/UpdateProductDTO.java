package com.example.demostore.dtos;

import com.example.demostore.enums.ProductCategory;
import com.example.demostore.model.Product;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateProductDTO(String name, String description, BigDecimal price, int quantity, String sku, ProductCategory category, boolean active) {
    public static UpdateProductDTO fromEntity(Product product) {
        return UpdateProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .sku(product.getSku())
                .category(product.getCategory())
                .active(product.isActive())
                .build();
    }
}
