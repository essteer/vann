package com.vann.services;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.Product;
import com.vann.models.enums.*;
import com.vann.repositories.ProductRepo;
import com.vann.utils.LogHandler;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoryService categoryService;

    public ProductService(ProductRepo productRepo, CategoryService categoryService) {
        this.productRepo = productRepo;
        this.categoryService = categoryService;
    }

    public Product createProduct(UUID categoryId, String name, double price, String image, Size size, Colour colour) throws RecordNotFoundException {
        Product product = new Product(categoryId, name, price, image, size, colour);
        return saveProduct(product);
    }

    private Product saveProduct(Product product) throws RecordNotFoundException {
        isValidCategoryId(product.getCategoryId());
        product.generateIdIfAbsent();
        return productRepo.save(product);
    }

    private void isValidCategoryId(UUID categoryId) throws RecordNotFoundException {
        try {
            categoryService.findCategoryById(categoryId);
            LogHandler.validIdOK(ProductService.class, categoryId, "Category");
        } catch (RecordNotFoundException e) {
            LogHandler.invalidIdNotFound(ProductService.class, categoryId, "Category");
            throw e;
        }
    }

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> findProductsByCategoryId(UUID categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    public List<Product> findProductsByName(String productName) {
        return productRepo.findByName(productName.toUpperCase());
    }

    public Product findProductById(UUID productId) throws RecordNotFoundException {
        Optional<Product> productOptional = productRepo.findById(productId);
        return productOptional.orElseThrow(() -> 
            new RecordNotFoundException("Product with ID '" + productId + "' not found"));
    }

    public Product updateProduct(UUID productId, Product updatedProduct) throws RecordNotFoundException {
        if (!productRepo.existsById(productId)) {
            throw new RecordNotFoundException("Product with ID '" + productId + "' not found");
        }
        isValidCategoryId(updatedProduct.getCategoryId());
        updatedProduct.setId(productId);
        updatedProduct.setName(updatedProduct.getName());
        return productRepo.save(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        productRepo.deleteById(productId);
    }

}
