package com.vann.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

@Entity
public class Product {

    @Id
    private UUID id;
    private String productName;
    private String productDesc;
    private double productPrice;
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "FK_category_id", referencedColumnName = "id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // Optional attribute
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true) // Optional attribute
    private Colour colour;

    public Product() {
    }

    public Product(String name, double price, String image, Category category, Size size, Colour colour) {
        generateId();
        this.productName = name.toUpperCase();
        this.productPrice = price;
        this.productImage = image;
        this.category = category;
        this.size = size;
        this.colour = colour;
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getProductId() {
        return id;
    }

    public void setProductId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName.toUpperCase();
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + productName + ", price=" + productPrice + ", category=" + category
                + ", size=" + size + ", colour=" + colour + "]";
    }

}
