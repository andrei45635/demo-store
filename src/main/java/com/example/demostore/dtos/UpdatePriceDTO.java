package com.example.demostore.dtos;

import com.example.demostore.model.Product;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdatePriceDTO(BigDecimal price) {
    public static UpdatePriceDTO fromEntity(Product product) {
        return UpdatePriceDTO.builder()
                .price(product.getPrice())
                .build();
    }
}
