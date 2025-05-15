package org.unibuc.demo.dto;

import java.util.List;

public class BasketResponseDTO {

    private List<BasketItemDTO> items;
    private Double total;

    public BasketResponseDTO() {}

    public BasketResponseDTO(List<BasketItemDTO> items, Double total) {
        this.items = items;
        this.total = total;
    }

    public List<BasketItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BasketItemDTO> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
