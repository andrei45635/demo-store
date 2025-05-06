package com.example.demostore.service;

import com.example.demostore.dtos.ProductDTO;
import com.example.demostore.dtos.ProductResponse;
import com.example.demostore.dtos.UpdatePriceDTO;
import com.example.demostore.dtos.UpdateProductDTO;
import com.example.demostore.enums.ProductCategory;
import com.example.demostore.exceptions.ResourceAlreadyExistsException;
import com.example.demostore.exceptions.ResourceNotFoundException;
import com.example.demostore.model.Product;
import com.example.demostore.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductDTO productDTO) {
        log.info("Creating new product with SKU: {}", productDTO.sku());

        if (productRepository.existsBySku(productDTO.sku())) {
            log.error("Product with SKU {} already exists", productDTO.sku());
            throw new ResourceAlreadyExistsException("Product", "SKU", productDTO.sku());
        }

        Product product = Product.builder()
                .name(productDTO.name())
                .description(productDTO.description())
                .price(productDTO.price())
                .quantity(productDTO.quantity())
                .sku(productDTO.sku())
                .category(productDTO.category())
                .active(true)
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return ProductResponse.fromEntity(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Retrieving product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found in getProductById()", id);
            return new ResourceNotFoundException("Product", "id", id);
        });

        return ProductResponse.fromEntity(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductBySku(String sku) {
        log.info("Retrieving product with SKU: {}", sku);

        Product product = productRepository.findBySku(sku).orElseThrow(() -> {
            log.error("Product with SKU {} not found in getProductBySku()", sku);
            return new ResourceNotFoundException("Product", "sku", sku);
        });

        return ProductResponse.fromEntity(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {
        log.info("Retrieving all products");
        return productRepository.findAll(pageable).map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        log.info("Retrieving all products by category: {}", category);
        return productRepository.findByCategory(category).stream().map(ProductResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {
        log.info("Retrieving all products with keyword: {}", keyword);
        return productRepository.searchByKeyword(keyword).stream().map(ProductResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(int quant) {
        log.info("Retrieving products with quantity less than {}", quant);
        return productRepository.findByQuantityLessThan(quant).stream().map(ProductResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Retrieving products within price range {}-{}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice).stream().map(ProductResponse::fromEntity).toList();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found in updateProduct()", id);
            return new ResourceNotFoundException("Product", "id", id);
        });

        Optional.ofNullable(productDTO.name()).ifPresent(product::setName);
        Optional.ofNullable(productDTO.description()).ifPresent(product::setDescription);
        Optional.ofNullable(productDTO.price()).ifPresent(product::setPrice);
        Optional.of(productDTO.quantity()).ifPresent(product::setQuantity);
        Optional.ofNullable(productDTO.category()).ifPresent(product::setCategory);
        Optional.of(productDTO.active()).ifPresent(product::setActive);

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());

        return ProductResponse.fromEntity(updatedProduct);
    }

    @Transactional
    public ProductResponse updateProductPrice(Long id, UpdatePriceDTO priceDTO) {
        log.info("Updating product price with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found in updateProductPrice()", id);
            return new ResourceNotFoundException("Product", "id", id);
        });

        product.setPrice(priceDTO.price());
        Product updatedProduct = productRepository.save(product);
        log.info("Price updated successfully for product with ID: {}", updatedProduct.getId());

        return ProductResponse.fromEntity(updatedProduct);
    }

    @Transactional
    public ProductResponse updateStock(Long id, Integer quantity) {
        log.info("Updating product stock with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found in updateStock()", id);
            return new ResourceNotFoundException("Product", "id", id);
        });

        int newQuantity = product.getQuantity() + quantity;
        if(newQuantity < 0) {
            log.error("Insufficient stock for product with ID: {}", id);
            throw new ResourceNotFoundException("Product", "id", id);
        }

        product.setQuantity(newQuantity);
        Product updatedProduct = productRepository.save(product);
        log.info("Stock updated successfully for product with ID: {}", updatedProduct.getId());

        return ProductResponse.fromEntity(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("Product with ID {} not found in deleteProduct()", id);
            return new ResourceNotFoundException("Product", "id", id);
        });

        productRepository.delete(product);
        log.info("Product deleted successfully with ID: {}", id);
    }

}
