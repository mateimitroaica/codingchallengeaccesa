package org.unibuc.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "store", "date", "price"})
})
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private String currency;

    private String store;
    private LocalDate date;

    @ManyToOne(optional = false)
    private Product product;

    public StoreProduct() {}

    public StoreProduct(Double price, String currency, String store, LocalDate date, Product product) {
        this.price = price;
        this.currency = currency;
        this.store = store;
        this.date = date;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
