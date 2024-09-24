package com.vann.services;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vann.exceptions.*;
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

    @Transactional
    public List<Product> createMany(List<Product> potentialProducts) {
        List<Product> savedProducts = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (Product potentialProduct : potentialProducts) {
            try {
                Product savedProduct = create(potentialProduct);
                savedProducts.add(savedProduct);
            } catch (IllegalArgumentException e) {
                errorMessages.add(e.getMessage());
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }

        if (errorMessages.isEmpty()) {
            LogHandler.status201Created(ProductService.class + " | " + savedProducts.size() + " records created");
            return savedProducts;

        } else {
            throw new BulkOperationException(errorMessages);
        }
    }

    @Transactional
    public Product createOne(Product potentialProduct) {
        Product product = create(potentialProduct);
        LogHandler.status201Created(ProductService.class + " | 1 record created");
        return product;
    }

    @Transactional
    public Product create(Product potentialProduct) throws RecordNotFoundException {
        Category partialCategory = potentialProduct.getCategory();
        Category category = categoryService.findCategoryByName(partialCategory.getName());

        String name = potentialProduct.getName();
        double price = potentialProduct.getPrice();
        String imageURI = potentialProduct.getImageURI();
        Size size = potentialProduct.getSize();
        Colour colour = potentialProduct.getColour();
        boolean featuredStatus = potentialProduct.getFeaturedStatus();

        validateProductAttributes(name, price, imageURI);
        Product product = new Product(category, name, price, imageURI, size, colour, featuredStatus);
        return saveProduct(product);
    }

    private void validateProductAttributes(String name, double price, String imageURI) {
        List<String> errorMessages = new ArrayList<>();
    
        if (name == null || name.trim().isEmpty()) {
            errorMessages.add(" name cannot be null or empty |");
        }
        if (imageURI == null || imageURI.trim().isEmpty()) {
            errorMessages.add(" imageURI cannot be null or empty |");
        }
        if (price <= 0) {
            errorMessages.add(" price must be greater than zero |");
        }
        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException(ProductService.class + " | " + String.join(" ", errorMessages));
        }
    }

    @Transactional
    private Product saveProduct(Product product) throws RecordNotFoundException {
        isValidCategoryId(product.getCategory().getId());
        return productRepo.save(product);
    }

    private void isValidCategoryId(UUID id) {
        categoryService.findCategoryById(id);
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepo.findAll();
        logBulkFindOperation(products, "findAll()");
        return products;
    }

    public List<Product> findProductsByCategoryId(UUID id) {
        List<Product> products =  productRepo.findByCategoryId(id);
        logBulkFindOperation(products, "id=" + id);
        return products;
    }

    public List<Product> findProductsByName(String name) {
        List<Product> products =  productRepo.findByName(name.toUpperCase());
        logBulkFindOperation(products, "name=" + name);
        return products;
    }

    public List<Product> findProductsByFeaturedStatus(boolean featuredStatus) {
        List<Product> featuredProducts = productRepo.findByFeaturedStatus(true);
        logBulkFindOperation(featuredProducts, "featuredStatus=" + featuredStatus);
        return featuredProducts;
    }

    private void logBulkFindOperation(List<Product> products, String details) {
        if (products.isEmpty()) {
            LogHandler.status204NoContent(ProductService.class + " | 0 records | " + details);    
        } else {
            LogHandler.status200OK(ProductService.class + " | " + products.size() + " records | " + details);
        }
    }

    public Product findProductById(UUID id) throws RecordNotFoundException {
        Optional<Product> productOptional = productRepo.findById(id);

        if (productOptional.isPresent()) {
            LogHandler.status200OK(ProductService.class + " | record found | id=" + id);
            return productOptional.get();
        } else {
            throw new RecordNotFoundException(ProductService.class + " | record not found | id=" + id);
        }
    }

    @Transactional
    public Product updateProduct(UUID id, Product updatedProduct) throws IllegalArgumentException, RecordNotFoundException {
        Product existingProduct = productRepo.findById(id).orElseThrow(() -> 
            new RecordNotFoundException(ProductService.class + " - record not found: " + id)
        );
        try {
            validateProductAttributes(updatedProduct.getName(), updatedProduct.getPrice(), updatedProduct.getImageURI());
        } catch (IllegalArgumentException e) {
            throw e;
        }

        existingProduct.setCategory(categoryService.findCategoryByName(updatedProduct.getCategory().getName()));
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageURI(updatedProduct.getImageURI());
        existingProduct.setSize(updatedProduct.getSize());
        existingProduct.setColour(updatedProduct.getColour());

        Product savedProduct = saveProduct(existingProduct);
        LogHandler.status200OK(ProductService.class + " | record updated | id=" + id);

        return savedProduct;
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            LogHandler.status204NoContent(ProductService.class + " | record deleted | id=" + id);
        }
    }

}
