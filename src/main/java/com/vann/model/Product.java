package com.vann.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;
import com.vann.utils.LogHandler;

@Entity
public class Product {

    @Id
    private UUID id;

    @Column(name = "FK_category_id", nullable = false)
    private UUID categoryId;

    private String productName;
    private double productPrice;
    private String productImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Colour colour;

    public Product() {
    }

    public Product(UUID categoryId, String name, double price, String image, Size size, Colour colour) {
        try {
            generateIdIfAbsent();
            this.categoryId = categoryId;
            this.productName = name.toUpperCase();
            this.productPrice = price;
            this.productImage = image;
            setColour(colour);
            setSize(size);
            LogHandler.createInstanceOK(Product.class, this.id, categoryId, name, price, image, size, colour);
        } catch (Exception e) {
            LogHandler.createInstanceError(Product.class, categoryId, name, price, image, size, colour);
            throw e;
        }
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getProductId() {
        generateIdIfAbsent();
        return id;
    }

    public void setProductId(UUID id) {
        if (id == null) {
            LogHandler.nullAttributeWarning(Product.class, getProductId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(Product.class, getProductId(), "id", String.valueOf(getProductId()));
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName.toUpperCase();
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

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) throws IllegalArgumentException {
        if (size == null) {
            this.size = size;
        } else {
            try {
                Size.valueOf(size.name());
                this.size = size;
                LogHandler.validAttributeOK(Product.class, getProductId(), "Size", String.valueOf(size));
            } catch (IllegalArgumentException e) {
                LogHandler.invalidAttributeError(Product.class, getProductId(), "Size", String.valueOf(size), e.getMessage());
                throw e;
            }
        }
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) throws IllegalArgumentException {
        this.colour = colour;
        if (colour == null) {
            this.colour = colour;
        } else {
            try {
                Colour.valueOf(colour.name());
                this.colour = colour;
                LogHandler.validAttributeOK(Product.class, getProductId(), "Colour", String.valueOf(colour));
            } catch (IllegalArgumentException e) {
                LogHandler.invalidAttributeError(Product.class, getProductId(), "Colour", String.valueOf(colour), e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", categoryId=" + categoryId + ", name=" + productName + ", unitprice=" + productPrice
                + ", size=" + size + ", colour=" + colour + "]";
    }

}
