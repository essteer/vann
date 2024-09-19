package com.vann.models;

import jakarta.persistence.*;

import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vann.models.enums.*;
import com.vann.utils.LogHandler;


@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID id;

    @Column(name = "FK_category_id", nullable = false)
    private UUID categoryId;

    private String name;
    private double price;
    private String imageURI;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Colour colour;

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date creationDate;

    public Product() {
    }

    public Product(UUID categoryId, String name, double price, String image, Size size, Colour colour) {
        try {
            generateIdIfAbsent();
            this.categoryId = categoryId;
            this.name = name.toUpperCase();
            this.price = price;
            this.imageURI = image;
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

    public UUID getId() {
        generateIdIfAbsent();
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            LogHandler.nullAttributeWarning(Product.class, getId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(Product.class, getId(), "id", String.valueOf(getId()));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name.toUpperCase();
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
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
                LogHandler.validAttributeOK(Product.class, getId(), "Size", String.valueOf(size));
            } catch (IllegalArgumentException e) {
                LogHandler.invalidAttributeError(Product.class, getId(), "Size", String.valueOf(size), e.getMessage());
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
                LogHandler.validAttributeOK(Product.class, getId(), "Colour", String.valueOf(colour));
            } catch (IllegalArgumentException e) {
                LogHandler.invalidAttributeError(Product.class, getId(), "Colour", String.valueOf(colour), e.getMessage());
                throw e;
            }
        }
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", categoryId=" + categoryId + ", name=" + name + ", unitprice=" + price
                + ", size=" + size + ", colour=" + colour + "]";
    }

}
