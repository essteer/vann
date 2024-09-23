package com.vann.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vann.models.enums.Colour;
import com.vann.models.enums.Size;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_category_id", nullable = false)
    @NotNull(message = "Category cannot be null")
    private Category category;

    @NotBlank(message = "Name cannot be null or empty")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
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

    @Column(nullable = false)
    private boolean featuredStatus = false;

    public Product() {
    }

    public Product(Category category, String name, double price, String imageURI, Size size, Colour colour) {
        this.category = category;
        this.name = (name != null) ? name.toUpperCase() : null;
        this.price = price;
        this.imageURI = imageURI;
        this.size = size;
        this.colour = colour;
    }

    public Product(Category category, String name, double price, String imageURI, Size size, Colour colour, boolean featuredStatus) {
        this(category, name, price, imageURI, size, colour);
        this.featuredStatus = featuredStatus;
    }

    public UUID getId() {
        return id;
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

    public Date getCreationDate() {
        return this.creationDate;
    }

    public boolean getFeaturedStatus() {
        return this.featuredStatus;
    }

    public void setFeaturedStatus(boolean featuredStatus) {
        this.featuredStatus = featuredStatus;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", category_name=" + category.getName() + ", name=" + name + ", unitprice=" + price
                + ", size=" + size + ", colour=" + colour + ", featuredStatus=" + featuredStatus +  "]";
    }

}
