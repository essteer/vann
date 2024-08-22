package com.vann.service;

import com.vann.exceptions.ProductNotFoundException;
import com.vann.model.Product;
import com.vann.repositories.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public Product findProductById(UUID productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        return productOptional.orElseThrow(() -> 
            new ProductNotFoundException("Product with ID '" + productId + "' not found"));
    }

    public Product updateProduct(UUID productId, Product updatedProduct) {
        // Check if the Product exists
        if (!productRepo.existsById(productId)) {
            throw new ProductNotFoundException("Product with ID '" + productId + "' not found");
        }
        // Set the ID of the updated Product
        updatedProduct.setProductId(productId);
        // Save the updated Product
        return productRepo.save(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        productRepo.deleteById(productId);
    }

}
