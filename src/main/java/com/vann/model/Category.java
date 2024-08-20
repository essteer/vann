package com.vann.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Category {

    @Id
    private UUID categoryUuid;
    private String categoryName;
    private String categoryDesc;

    public Category() {
        this.categoryUuid = UUID.randomUUID();
    }

    public Category(String name, String desc) {
        this();
        this.categoryName = name;
        this.categoryDesc = desc;
    }

    public UUID getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(UUID categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    @Override
    public String toString() {
        return "Category [categoryUuid=" + categoryUuid + ", categoryName=" + categoryName + ", categoryDesc="
                + categoryDesc + "]";
    }

}
