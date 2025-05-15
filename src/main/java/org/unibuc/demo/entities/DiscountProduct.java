package org.unibuc.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DiscountProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalDate discountStartingDate;
    private LocalDate discountEndingDate;
    private Double percentage;
    private String store;

    @ManyToOne(optional = false)
    private Product product;

    public DiscountProduct() {}

    public DiscountProduct(LocalDate date, LocalDate discountStartingDate, LocalDate discountEndingDate, Double percentage, Product product, String store) {
        this.date = date;
        this.discountStartingDate = discountStartingDate;
        this.discountEndingDate = discountEndingDate;
        this.percentage = percentage;
        this.product = product;
        this.store = store;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDiscountStartingDate() {
        return discountStartingDate;
    }

    public void setDiscountStartingDate(LocalDate discountStartingDate) {
        this.discountStartingDate = discountStartingDate;
    }

    public LocalDate getDiscountEndingDate() {
        return discountEndingDate;
    }

    public void setDiscountEndingDate(LocalDate discountEndingDate) {
        this.discountEndingDate = discountEndingDate;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
