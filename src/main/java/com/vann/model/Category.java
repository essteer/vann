package com.vann.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.util.UUID;

import com.vann.model.enums.CategoryType;
import com.vann.utils.LogHandler;

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
        try {
            generateIdIfAbsent();
            setCategoryType(categoryType);
            setCategoryName(name);
            LogHandler.createInstanceOK(Category.class, this.id, String.valueOf(this.categoryType), this.categoryName);
        } catch (Exception e) {
            LogHandler.createInstanceError(Category.class, String.valueOf(this.categoryType), this.categoryName);
            throw e;
        }
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCategoryId() {
        generateIdIfAbsent();
        return id;
    }

    public void setCategoryId(UUID id) {
        this.id = id;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) throws IllegalArgumentException {
        if (categoryType == null) {
            LogHandler.nullAttributeWarning(Category.class, getCategoryId(), "CategoryType");
        }
        try {
            CategoryType.valueOf(categoryType.name());
            this.categoryType = categoryType;
            LogHandler.validAttributeOK(Category.class, getCategoryId(), "CategoryType", String.valueOf(categoryType));
        } catch (IllegalArgumentException e) {
            LogHandler.invalidAttributeError(Category.class, getCategoryId(), "CategoryType", String.valueOf(categoryType), e.getMessage());
            throw e;
        }
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            LogHandler.nullAttributeWarning(Category.class, getCategoryId(), "name");
        } else {
            try{
                this.categoryName = categoryName.toLowerCase();
                LogHandler.validAttributeOK(Category.class, getCategoryId(), "name", getCategoryName());
            } catch (Exception e) {
                LogHandler.invalidAttributeError(Category.class, getCategoryId(), "name", categoryName, e.getMessage());
                throw e;
            } 
        }
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", type=" + categoryType + ", name="
                + categoryName + "]";
    }

}
