package org.unibuc.demo.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productName) {
        super("Product " + productName + " not found.");
    }
}
