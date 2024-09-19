package com.vann.services;

import com.vann.models.Product;
import com.vann.repositories.ProductRepo;

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
    void testFindProductById() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Necklace");

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findProductById(productId);
        assertEquals("NECKLACE", foundProduct.getName());
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