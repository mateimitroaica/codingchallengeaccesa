package org.unibuc.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class ProductPriceHistoryDTO {
    private String productId;
    private String productName;
    private String brand;
    private String productCategory;
    private List<PriceHistoryPoint> dataPoints;

    public ProductPriceHistoryDTO() {}

    public ProductPriceHistoryDTO(String productId, String productName, String brand, String productCategory, List<PriceHistoryPoint> dataPoints) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.productCategory = productCategory;
        this.dataPoints = dataPoints;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public List<PriceHistoryPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<PriceHistoryPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public static class PriceHistoryPoint {
        private LocalDate date;
        private Double price;
        private Boolean discounted;
        private String store;

        public PriceHistoryPoint(){}

        public PriceHistoryPoint(LocalDate date, Double price, Boolean discounted, String store) {
            this.date = date;
            this.price = price;
            this.discounted = discounted;
            this.store = store;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Boolean isDiscounted() {
            return discounted;
        }

        public void setDiscounted(Boolean discounted) {
            this.discounted = discounted;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }
    }
}
