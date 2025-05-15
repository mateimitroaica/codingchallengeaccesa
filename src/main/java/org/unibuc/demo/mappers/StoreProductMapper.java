package org.unibuc.demo.mappers;

import org.springframework.stereotype.Component;
import org.unibuc.demo.dto.BasketItemDTO;
import org.unibuc.demo.entities.StoreProduct;

@Component
public class StoreProductMapper {

    public BasketItemDTO toDTO(StoreProduct product) {
        BasketItemDTO dto = new BasketItemDTO();

        dto.setProductName(product.getProduct().getProductName());
        dto.setBrand(product.getProduct().getBrand());
        dto.setPrice(product.getPrice());
        dto.setCurrency(product.getCurrency());
        dto.setStore(product.getStore());

        return dto;
    }
}
