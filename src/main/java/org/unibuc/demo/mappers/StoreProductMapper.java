package org.unibuc.demo.mappers;

import org.springframework.stereotype.Component;
import org.unibuc.demo.dto.BasketItemDTO;
import org.unibuc.demo.dto.DiscountProductDTO;
import org.unibuc.demo.entities.DiscountProduct;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.util.Optional;

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

    public DiscountProductDTO toDiscountDTO(StoreProduct product, DiscountProduct discountProduct) {
        DiscountProductDTO dto = new DiscountProductDTO();

        Double originalPrice = product.getPrice();
        Double discountedPrice = originalPrice * (1 - discountProduct.getPercentage() / 100.0);
        dto.setOriginalPrice(product.getPrice());
        dto.setStore(product.getStore());
        dto.setDiscountPercentage(discountProduct.getPercentage());
        dto.setDiscountedPrice(discountedPrice);
        dto.setDiscountStart(discountProduct.getDiscountStartingDate());
        dto.setDiscountEnd(discountProduct.getDiscountEndingDate());

        return dto;
    }

    public DiscountProductDTO mapDiscountAndStoreProduct(DiscountProduct discountProduct, StoreProductRepository storeProductRepository) {
        Product product = discountProduct.getProduct();

        Optional<StoreProduct> optStoreProd = storeProductRepository.findByProductAndStoreAndDate(
                product, discountProduct.getStore(), discountProduct.getDate()
        );

        if (optStoreProd.isEmpty()) {
            return null;
        }

        StoreProduct storeProduct = optStoreProd.get();
        DiscountProductDTO dto = toDiscountDTO(storeProduct, discountProduct);
        dto.setProductName(product.getProductName());
        dto.setBrand(product.getBrand());

        return dto;
    }
}
