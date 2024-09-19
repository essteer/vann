package com.vann.models;

import jakarta.persistence.*;

import java.util.UUID;

import com.vann.models.enums.CategoryType;


@Entity
@Table(name = "categories")
public class Category {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(unique = true, nullable = false)
    private String name;

    public Category() {
    }

    public Category(CategoryType categoryType, String name) {
        generateIdIfAbsent();
        setType(categoryType);
        setName(name);
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
        if (id != null) {
            this.id = id;
        }
    }

    public CategoryType getType() {
        return categoryType;
    }

    public void setType(CategoryType categoryType) {
        if (categoryType != null) {
            this.categoryType = categoryType;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !(name.trim().isEmpty())) {
            this.name = name.toLowerCase();
        }
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", categoryType=" + categoryType + ", name="
                + name + "]";
    }

}
