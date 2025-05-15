package org.unibuc.demo.dto;

import java.time.LocalDate;

public class DiscountProductDTO {
    private String productName;
    private String brand;
    private Double originalPrice;
    private Double discountPercentage;
    private Double discountedPrice;
    private String store;
    private LocalDate discountStart;
    private LocalDate discountEnd;

    public DiscountProductDTO() {}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public LocalDate getDiscountStart() {
        return discountStart;
    }

    public void setDiscountStart(LocalDate discountStart) {
        this.discountStart = discountStart;
    }

    public LocalDate getDiscountEnd() {
        return discountEnd;
    }

    public void setDiscountEnd(LocalDate discountEnd) {
        this.discountEnd = discountEnd;
    }
}
