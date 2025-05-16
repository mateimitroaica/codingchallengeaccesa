package org.unibuc.demo.dto;

import java.time.LocalDate;

public class PriceAlertCheckRequest {
    private String productName;
    private Double targetPrice;
    private LocalDate date;

    public PriceAlertCheckRequest() {}

    public PriceAlertCheckRequest(String productName, Double targetPrice, LocalDate date) {
        this.productName = productName;
        this.targetPrice = targetPrice;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
