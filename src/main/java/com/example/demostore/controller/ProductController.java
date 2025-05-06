package com.example.demostore.controller;

import com.example.demostore.dtos.ProductDTO;
import com.example.demostore.dtos.ProductResponse;
import com.example.demostore.dtos.UpdatePriceDTO;
import com.example.demostore.dtos.UpdateProductDTO;
import com.example.demostore.enums.ProductCategory;
import com.example.demostore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProductResponse createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public Page<ProductResponse> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return productService.getAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public ProductResponse getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public List<ProductResponse> getProductByCategory(@PathVariable ProductCategory category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public List<ProductResponse> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public List<ProductResponse> getLowStockProducts(@RequestParam int quantity) {
        return productService.getLowStockProducts(quantity);
    }

    @GetMapping("/price-range")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'EMPLOYEE', 'CUSTOMER')")
    public List<ProductResponse> getProductsByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        return productService.getProductsInPriceRange(minPrice, maxPrice);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @PutMapping("/{id}/price")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProductResponse updateProductPrice(@PathVariable Long id, @RequestBody UpdatePriceDTO productDTO) {
        return productService.updateProductPrice(id, productDTO);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ProductResponse updateProductStock(@PathVariable Long id, @RequestParam int quantity) {
        return productService.updateStock(id, quantity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
