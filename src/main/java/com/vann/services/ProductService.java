package com.vann.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.models.enums.*;
import com.vann.repositories.ProductRepo;
import com.vann.utils.LogHandler;


@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoryService categoryService;

    public ProductService(ProductRepo productRepo, CategoryService categoryService) {
        this.productRepo = productRepo;
        this.categoryService = categoryService;
    }

    public Product createProduct(Category partialCategory, String name, double price, String image, Size size, Colour colour) throws RecordNotFoundException {
        Category category = categoryService.findCategoryByName(partialCategory.getName());
        Product product = new Product(category, name, price, image, size, colour);
        return saveProduct(product);
    }

    private Product saveProduct(Product product) throws RecordNotFoundException {
        isValidCategoryId(product.getCategory().getId());
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

    public List<Product> findProductsByName(String name) {
        return productRepo.findByName(name.toUpperCase());
    }

    public Product findProductById(UUID id) throws RecordNotFoundException {
        Optional<Product> productOptional = productRepo.findById(id);
        return productOptional.orElseThrow(() -> 
            new RecordNotFoundException("Product with ID '" + id + "' not found"));
    }

    public Product updateProduct(UUID id, Product updatedProduct) throws RecordNotFoundException {
        if (!productRepo.existsById(id)) {
            throw new RecordNotFoundException("Product with ID '" + id + "' not found");
        }
        Category partialCategory = updatedProduct.getCategory();
        Category category = categoryService.findCategoryByName(partialCategory.getName());
        updatedProduct.setId(id);
        updatedProduct.setCategory(category);
        updatedProduct.setName(updatedProduct.getName());
        return productRepo.save(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        productRepo.deleteById(id);
    }

}
