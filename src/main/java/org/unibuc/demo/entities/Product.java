package org.unibuc.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productName", "brand", "packageQuantity","packageUnit"})
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private Double packageQuantity;
    private String packageUnit;

    public Product() {}

    public Product(String productId, String productName, String productCategory, String brand, Double packageQuantity, String packageUnit) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(Double packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }
}
