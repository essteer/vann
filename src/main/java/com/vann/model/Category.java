package com.vann.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

import com.vann.model.enums.CategoryType;

@Entity
public class Category {

    @Id
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    private String categoryName;

    public Category() {
        this.categoryId = UUID.randomUUID();
    }

    public Category(CategoryType categoryType, String name) {
        this();
        this.categoryType = categoryType;
        this.categoryName = name;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
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
        return "Category [categoryId=" + categoryId + ", categoryType=" + categoryType + ", categoryName="
                + categoryName + "]";
    }

}
