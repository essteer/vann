package com.vann.service;

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

    public Optional<Product> findProductById(UUID productUuid) {
        return productRepo.findById(productUuid);
    }

    public Product updateProduct(UUID productUuid, Product updatedProduct) {
        // Check if the Product exists
        if (!productRepo.existsById(productUuid)) {
            throw new IllegalArgumentException("Product not found");
        }
        // Set the ID of the updated Product
        updatedProduct.setProductUuid(productUuid);
        // Save the updated Product
        return productRepo.save(updatedProduct);
    }

    public void deleteProduct(UUID productUuid) {
        productRepo.deleteById(productUuid);
    }

}
