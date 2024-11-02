package com.vann.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vann.models.enums.Colour;
import com.vann.models.enums.Size;


@Entity
@Table(name = "products")
@Data
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
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

    public void setName(String name) {
        if (name != null) {
            this.name = name.toUpperCase();
        }
    }

    public boolean getFeaturedStatus() {
        return this.featuredStatus;
    }

}
