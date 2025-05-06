package com.example.demostore.repo;

import com.example.demostore.enums.ProductCategory;
import com.example.demostore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByQuantityLessThan(int quant);

    @Query("select p from products p where p.price between :minPrice and :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("select p from products p where lower(p.name) like lower(concat('%', :keyword, '%')) or lower(p.description) like lower(concat('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

}
