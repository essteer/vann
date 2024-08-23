package com.vann.services;

import com.vann.model.Product;
import com.vann.repositories.ProductRepo;
import com.vann.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setProductName("Necklace");
        product.setProductPrice(100.0);

        when(productRepo.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals("NECKLACE", savedProduct.getProductName());
        assertEquals(100.0, savedProduct.getProductPrice());
        verify(productRepo, times(1)).save(product);
    }

    @Test
    void testFindProductById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Necklace");

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findProductById(productId);
        assertEquals("NECKLACE", foundProduct.getProductName());
        verify(productRepo, times(1)).findById(productId);
    }

    @Test
    void testDeleteProduct() {
        UUID productId = UUID.randomUUID();
        doNothing().when(productRepo).deleteById(productId);
        productService.deleteProduct(productId);
        verify(productRepo, times(1)).deleteById(productId);
    }
}