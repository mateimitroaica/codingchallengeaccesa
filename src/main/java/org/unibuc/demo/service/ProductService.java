package org.unibuc.demo.service;

import org.springframework.stereotype.Service;
import org.unibuc.demo.dto.BasketItemDTO;
import org.unibuc.demo.dto.BasketResponseDTO;
import org.unibuc.demo.dto.DiscountProductDTO;
import org.unibuc.demo.entities.DiscountProduct;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.mappers.StoreProductMapper;
import org.unibuc.demo.repositories.DiscountProductRepository;
import org.unibuc.demo.repositories.ProductRepository;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final DiscountProductRepository discountProductRepository;
    private final StoreProductMapper storeProductMapper;

    public ProductService(ProductRepository productRepository, StoreProductRepository storeProductRepository,
                          StoreProductMapper storeProductMapper, DiscountProductRepository discountProductRepository) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
        this.storeProductMapper = storeProductMapper;
        this.discountProductRepository = discountProductRepository;
    }

    public BasketResponseDTO basketMonitoring(LocalDate date, List<String> productNames) {
        List<BasketItemDTO> result = new ArrayList<>();

        LocalDate selectedDate;
        if (date.getDayOfMonth() < 8) {
            selectedDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        } else {
            selectedDate = LocalDate.of(date.getYear(), date.getMonth(), 8);
        }

        for (String productName : productNames) {
            Optional<StoreProduct> prd = storeProductRepository.findByProduct_ProductNameAndDate(productName,selectedDate)
                    .stream()
                    .min(Comparator.comparingDouble(StoreProduct::getPrice));

            prd.ifPresent(
                    x -> result.add(storeProductMapper.toDTO(x))
            );
        }

        result.sort(Comparator.comparing(BasketItemDTO::getProductName));

        Double totalSum = result.stream().map(BasketItemDTO::getPrice).reduce(0.0, Double::sum);

        return new BasketResponseDTO(result, totalSum);
    }

    public List<DiscountProductDTO> getBestCurrentDiscounts(LocalDate currentDate) {
        List<DiscountProductDTO> result = new ArrayList<>();

        List<DiscountProduct> activeDiscounts = discountProductRepository
                .findByDiscountStartingDateLessThanEqualAndDiscountEndingDateGreaterThanEqual(currentDate, currentDate);

        for (DiscountProduct discountProduct : activeDiscounts) {
            DiscountProductDTO dto = storeProductMapper.mapDiscountAndStoreProduct(discountProduct, storeProductRepository);
            if (dto != null) {
                result.add(dto);
            }
        }

        result.sort(Comparator.comparingDouble(DiscountProductDTO::getDiscountPercentage).reversed());

        return result;
    }

    public List<DiscountProductDTO> getNewDiscounts(LocalDate toDate) {
        List<DiscountProductDTO> result = new ArrayList<>();
        LocalDate from = toDate.minusDays(1);

        List<DiscountProduct> recentDiscounts = discountProductRepository
                .findByDiscountStartingDateBetween(from, toDate);

        for (DiscountProduct discount : recentDiscounts) {
            DiscountProductDTO dto = storeProductMapper.mapDiscountAndStoreProduct(discount, storeProductRepository);
            if (dto != null) {
                result.add(dto);
            }
        }

        result.sort(Comparator.comparingDouble(DiscountProductDTO::getDiscountedPrice));

        return result;
    }


}