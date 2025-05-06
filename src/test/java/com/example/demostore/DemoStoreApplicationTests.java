package com.example.demostore;

import com.example.demostore.dtos.ProductDTO;
import com.example.demostore.dtos.ProductResponse;
import com.example.demostore.dtos.UpdatePriceDTO;
import com.example.demostore.enums.ProductCategory;
import com.example.demostore.exceptions.BadRequestException;
import com.example.demostore.exceptions.ResourceAlreadyExistsException;
import com.example.demostore.exceptions.ResourceNotFoundException;
import com.example.demostore.model.Product;
import com.example.demostore.repo.ProductRepository;
import com.example.demostore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemoStoreApplicationTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("19.99"));
        product.setQuantity(100);
        product.setSku("TEST-123");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

//        productDTO = new ProductDTO();
//        productDTO.name("New Product");
//        productDTO.description("New Description");
//        productDTO.setPrice(new BigDecimal("29.99"));
//        productDTO.setStockQuantity(50);
//        productDTO.setSku("NEW-123");
//        productDTO.setCategory(ProductCategory.BOOKS);

//        product = Product.builder()
//                .id(1L)
//                .name("test product")
//                .description("test")
//                .price(new BigDecimal("19.99"))
//                .quantity(100)
//                .sku("TEST-123")
//                .category(ProductCategory.ELECTRONICS)
//                .active(true)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
        productDTO = ProductDTO.builder()
                .name("new product")
                .description("new")
                .price(new BigDecimal("29.99"))
                .quantity(50)
                .sku("NEW-123")
                .category(ProductCategory.BOOKS)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(productDTO);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getPrice(), response.getPrice());

        verify(productRepository).existsBySku(productDTO.sku());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithExistingSku() {
        when(productRepository.existsBySku(anyString())).thenReturn(true);
        
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            productService.createProduct(productDTO);
        });

        verify(productRepository).existsBySku(productDTO.sku());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldGetProductByIdSuccessfully() {
         
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getName(), response.getName());

        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundById() {

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        verify(productRepository).findById(1L);
    }

    @Test
    void shouldUpdateProductPriceSuccessfully() {
         
        UpdatePriceDTO priceRequest = new UpdatePriceDTO(new BigDecimal("39.99"));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProductPrice(1L, priceRequest);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getPrice(), response.getPrice());

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldUpdateStockSuccessfully() {
         
        int quantityChange = 10;

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateStock(1L, quantityChange);

        assertNotNull(response);
        assertEquals(product.getId(), response.getId());
        assertEquals(product.getQuantity(), response.getStockQuantity());

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStockBelowZero() {
         
        product.setQuantity(10);
        int quantityChange = -20;

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () -> {
            productService.updateStock(1L, quantityChange);
        });

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldGetProductsByCategorySuccessfully() {
         
        when(productRepository.findByCategory(any(ProductCategory.class)))
                .thenReturn(List.of(product));

        List<ProductResponse> responses = productService.getProductsByCategory(ProductCategory.ELECTRONICS);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(product.getId(), responses.get(0).getId());
        assertEquals(product.getCategory(), responses.get(0).getCategory());

        verify(productRepository).findByCategory(ProductCategory.ELECTRONICS);
    }

}
