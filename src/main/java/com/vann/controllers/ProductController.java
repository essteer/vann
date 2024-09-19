package com.vann.controllers;

import java.net.URI;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.exceptions.*;
import com.vann.models.*;
import com.vann.services.*;
import com.vann.utils.LogHandler;


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
    public ResponseEntity<?> getByCategoryName(@PathVariable String categoryName) {
        String methodName = "getByCategoryName()";
        try {
            Category category = categoryService.findCategoryByName(categoryName);
            List<Product> products = productService.findProductsByCategoryId(category.getId());
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
    public ResponseEntity<List<Product>> getByName(@PathVariable String productName) {
        String methodName = "getByName()";
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
    public ResponseEntity<?> get(@PathVariable UUID id) {
        String methodName = "get()";
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
                product.getCategory(),
                product.getName(),
                product.getPrice(),
                product.getImageURI(),
                product.getSize(),
                product.getColour()
            );
    
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
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
                        product.getCategory(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageURI(),
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
