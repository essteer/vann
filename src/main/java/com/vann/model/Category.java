package com.vann.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

import com.vann.model.enums.CategoryType;

@Entity
public class Category {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Column(unique = true)
    private String categoryName;

    public Category() {
    }

    public Category(CategoryType categoryType, String name) {
        generateId();
        this.categoryType = categoryType;
        this.categoryName = name;
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCategoryId() {
        if (this.id == null) {
            generateId();
        }
        return id;
    }

    public void setCategoryId(UUID id) {
        this.id = id;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", type=" + categoryType + ", name="
                + categoryName + "]";
    }

}
