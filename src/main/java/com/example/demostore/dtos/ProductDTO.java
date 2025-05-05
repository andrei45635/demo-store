package com.example.demostore.dtos;

import com.example.demostore.enums.ProductCategory;
import com.example.demostore.model.Product;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDTO(String name, String description, BigDecimal price, int quantity, String sku, ProductCategory category) {
    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .sku(product.getSku())
                .category(product.getCategory())
                .build();
    }
}
