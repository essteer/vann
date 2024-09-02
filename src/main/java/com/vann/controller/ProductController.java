package com.vann.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Category;
import com.vann.model.Product;
import com.vann.service.CategoryService;
import com.vann.service.ProductService;
import com.vann.utils.LogHandler;

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
        String methodName = "getAllProducts()";
        try {
            List<Product> products = productService.findAllProducts();
            if (products.isEmpty()) {
                LogHandler.status204NoContent("GET", ProductController.class, methodName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", ProductController.class, methodName);
            return ResponseEntity.ok(products);
        
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<?> getProductsByCategoryName(@PathVariable String categoryName) {
        String methodName = "getProductsByCategoryName()";
        try {
            Category category = categoryService.findCategoryByName(categoryName);
            List<Product> products = productService.findProductsByCategoryId(category.getCategoryId());
            if (products.isEmpty()) {
                LogHandler.status204NoContent("GET", ProductController.class, methodName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", ProductController.class, methodName);
            return ResponseEntity.ok(products);
        
        } catch (RecordNotFoundException e) {  // for categoryName not found
            LogHandler.status404NotFound("GET", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String productName) {
        String methodName = "getProductsByName()";
        try {
            List<Product> products = productService.findProductsByName(productName);
            if (products.isEmpty()) {
                LogHandler.status204NoContent("GET", ProductController.class, methodName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", ProductController.class, methodName);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("GET", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable UUID id) {
        String methodName = "getProduct()";
        try {    
            Product product = productService.findProductById(id);
            LogHandler.status200OK("GET", ProductController.class, methodName);
            return ResponseEntity.ok(product);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        String methodName = "createProduct()";
        try {
            Product savedProduct = productService.createProduct(
                product.getCategoryId(),
                product.getProductName(),
                product.getProductPrice(),
                product.getProductImage(),
                product.getSize(),
                product.getColour()
            );
    
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getProductId())
                .toUri();

            LogHandler.status201Created("POST", ProductController.class, methodName);
            return ResponseEntity.created(location).body(savedProduct);
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("POST", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("POST", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("POST", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createProducts(@RequestBody List<Product> products) {
        String methodName = "createProducts()";
        try {
            List<Product> savedProducts = new ArrayList<>();
            for (Product product : products) {
                try {
                    Product savedProduct = productService.createProduct(
                        product.getCategoryId(),
                        product.getProductName(),
                        product.getProductPrice(),
                        product.getProductImage(),
                        product.getSize(),
                        product.getColour()
                    );
                    savedProducts.add(savedProduct);
                } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
                    LogHandler.status400BadRequest("POST", ProductController.class, methodName, e.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                } catch (FieldConflictException e) {
                    LogHandler.status409Conflict("POST", ProductController.class, methodName, e.getMessage());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
                }
            }
            LogHandler.status201Created("POST", ProductController.class, methodName);
            return ResponseEntity.status(HttpStatus.OK).body(savedProducts);
        } catch (Exception e) {
            LogHandler.status500InternalServerError("POST", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody Product updatedProduct) {
        String methodName = "updateProduct()";
        try {
            Product updated = productService.updateProduct(id, updatedProduct);
            LogHandler.status200OK("PUT", ProductController.class, methodName);
            return ResponseEntity.ok(updated);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("PUT", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("PUT", ProductController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("PUT", ProductController.class, methodName, e.getMessage());
            throw e;
        }
    }
}
