package org.unibuc.demo.dto;

import java.util.List;

public class RecommendationDTO {
    private ProductDTO searchedProduct;
    private List<ProductDTO> recommendedAlternatives;

    public RecommendationDTO(ProductDTO searchedProduct, List<ProductDTO> recommendedAlternatives) {
        this.searchedProduct = searchedProduct;
        this.recommendedAlternatives = recommendedAlternatives;
    }

    public ProductDTO getSearchedProduct() {
        return searchedProduct;
    }

    public void setSearchedProduct(ProductDTO searchedProduct) {
        this.searchedProduct = searchedProduct;
    }

    public List<ProductDTO> getRecommendedAlternatives() {
        return recommendedAlternatives;
    }

    public void setRecommendedAlternatives(List<ProductDTO> recommendedAlternatives) {
        this.recommendedAlternatives = recommendedAlternatives;
    }

    public static class ProductDTO {
        private String productName;
        private String brand;
        private String store;
        private Double price;
        private Double quantity;
        private String unit;
        private Double valuePerUnit;

        public ProductDTO(String productName, String brand, String store, Double price, Double quantity, String unit, Double valuePerUnit) {
            this.productName = productName;
            this.brand = brand;
            this.store = store;
            this.price = price;
            this.quantity = quantity;
            this.unit = unit;
            this.valuePerUnit = valuePerUnit;
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

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Double getValuePerUnit() {
            return valuePerUnit;
        }

        public void setValuePerUnit(Double valuePerUnit) {
            this.valuePerUnit = valuePerUnit;
        }
    }

}
