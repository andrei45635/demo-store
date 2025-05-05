package com.example.demostore.model;

import com.example.demostore.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description", length = 500)
    private String description;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @Column(name="quantity", nullable = false)
    private Integer quantity;

    @Column(name="sku", nullable = false, unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(name="category", nullable = false)
    private ProductCategory category;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

}
