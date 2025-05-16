package org.unibuc.demo.dto;

import java.util.List;

public class PriceAlertCheckResponse {
    private Boolean matched;
    private List<MatchedStoreDTO> matchingStores;

    public static class MatchedStoreDTO {
        private String store;
        private Double finalPrice;
        private Boolean discounted;

        public MatchedStoreDTO(String store, Double finalPrice, Boolean discounted) {
            this.store = store;
            this.finalPrice = finalPrice;
            this.discounted = discounted;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public Double getFinalPrice() {
            return finalPrice;
        }

        public void setFinalPrice(Double finalPrice) {
            this.finalPrice = finalPrice;
        }

        public Boolean getDiscounted() {
            return discounted;
        }

        public void setDiscounted(Boolean discounted) {
            this.discounted = discounted;
        }
    }

    public PriceAlertCheckResponse(Boolean matched, List<MatchedStoreDTO> matchingStores) {
        this.matched = matched;
        this.matchingStores = matchingStores;
    }

    public Boolean isMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public List<MatchedStoreDTO> getMatchingStores() {
        return matchingStores;
    }

    public void setMatchingStores(List<MatchedStoreDTO> matchingStores) {
        this.matchingStores = matchingStores;
    }
}
