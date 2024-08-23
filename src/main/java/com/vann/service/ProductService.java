package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Category;
import com.vann.model.Product;
import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;
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

    public Product createProduct(String name, double price, String image, Category category, Size size, Colour colour) {
        if (category == null) {
            throw new RecordNotFoundException("Category not found");
        }
        Product product = new Product(name, price, image, category, size, colour);
        return saveProduct(product);
    }

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> findProductsByCategoryName(String categoryName) {
        return productRepo.findByCategory_CategoryName(categoryName);
    }

    public List<Product> findProductsByCategoryType(CategoryType categoryType) {
        return productRepo.findByCategory_CategoryType(categoryType);
    }

    public List<Product> findProductsByName(String productName) {
        return productRepo.findByProductName(productName.toUpperCase());
    }

    public Product findProductById(UUID productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        return productOptional.orElseThrow(() -> 
            new RecordNotFoundException("Product with ID '" + productId + "' not found"));
    }

    public Product saveProduct(Product product) {
        product.generateIdIfAbsent();
        return productRepo.save(product);
    }

    public Product updateProduct(UUID productId, Product updatedProduct) {
        if (!productRepo.existsById(productId)) {
            throw new RecordNotFoundException("Product with ID '" + productId + "' not found");
        }
        updatedProduct.setProductId(productId);
        updatedProduct.setProductName(updatedProduct.getProductName());
        return productRepo.save(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        productRepo.deleteById(productId);
    }

}
