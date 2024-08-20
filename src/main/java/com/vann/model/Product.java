package com.vann.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Product {

    @Id
    private UUID productUuid;
    private String productName;
    private String productDesc;
    private double productPrice;
    private String productStatus;
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "FK_categoryUuid", referencedColumnName = "categoryUuid")
    private Category category;

    public Product() {
        this.productUuid = UUID.randomUUID();
    }

    public Product(String name, String desc, double price, String status, String image, Category category) {
        this();
        this.productName = name;
        this.productDesc = desc;
        this.productPrice = price;
        this.productStatus = status;
        this.productImage = image;
        this.category = category;
    }

    public UUID getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(UUID productUuid) {
        this.productUuid = productUuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
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

    @Override
    public String toString() {
        return "Product [productUuid=" + productUuid + ", productName=" + productName + ", productDesc=" + productDesc
                + ", productPrice=" + productPrice + ", productStatus=" + productStatus + ", category=" + category
                + "]";
    }

}
