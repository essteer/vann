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
import com.vann.utils.LogHandler;

@Entity
public class Product {

    @Id
    private UUID id;
    private String productName;
    private double productPrice;
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "FK_category_id", referencedColumnName = "id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Colour colour;

    public Product() {
    }

    public Product(String name, double price, String image, Category category, Size size, Colour colour) {
        try {
            generateIdIfAbsent();
            this.productName = name.toUpperCase();
            this.productPrice = price;
            this.productImage = image;
            this.category = category;
            setColour(colour);
            setSize(size);
            LogHandler.createInstanceOK(Product.class, this.id, name, price, image, category, size, colour);
        } catch (Exception e) {
            LogHandler.createInstanceError(Product.class, name, price, image, category, size, colour);
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
        this.id = id;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        return "Product [id=" + id + ", name=" + productName + ", unitprice=" + productPrice + ", category=" + category
                + ", size=" + size + ", colour=" + colour + "]";
    }

}
