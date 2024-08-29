package com.vann.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Category;
import com.vann.model.Product;
import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;
import com.vann.service.CategoryService;
import com.vann.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(products);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategoryName(@PathVariable String categoryName) {
        List<Product> products = productService.findProductsByCategoryName(categoryName);
        return ResponseEntity.ok(products);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/category/type/{categoryType}")
    public ResponseEntity<List<Product>> getProductsByCategoryType(@PathVariable CategoryType categoryType) {
        List<Product> products = productService.findProductsByCategoryType(categoryType);
        return ResponseEntity.ok(products);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/name/{productName}")
    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String productName) {
        List<Product> products = productService.findProductsByName(productName);
        return ResponseEntity.ok(products);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable UUID id) {
        try {    
            Product product = productService.findProductById(id);
            return ResponseEntity.ok(product);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
        @RequestParam String productName,
        @RequestParam double productPrice,
        @RequestParam String productImage,
        @RequestParam String categoryName,
        @RequestParam(required = false) Colour colour,
        @RequestParam(required = false) Size size
    ) {
        try {
            Category category = categoryService.findCategoryByName(categoryName);
            Product product = productService.createProduct(productName, productPrice, productImage, category, size, colour);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getProductId())
                .toUri();
    
            return ResponseEntity.created(location).body(product);
        
        } catch (RecordNotFoundException e) {
            Exception elaboratedException = new RecordNotFoundException("Category with name '" + categoryName + "' not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(elaboratedException.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product updatedProduct) {
        try {
            Product updated = productService.updateProduct(id, updatedProduct);
            return ResponseEntity.ok(updated);
        } catch (FieldConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
