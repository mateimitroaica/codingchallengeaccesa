package org.unibuc.demo.service;

import org.springframework.stereotype.Service;
import org.unibuc.demo.dto.*;
import org.unibuc.demo.entities.DiscountProduct;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.exceptions.ProductNotFoundException;
import org.unibuc.demo.mappers.StoreProductMapper;
import org.unibuc.demo.repositories.DiscountProductRepository;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final StoreProductRepository storeProductRepository;
    private final DiscountProductRepository discountProductRepository;
    private final StoreProductMapper storeProductMapper;

    public ProductService(StoreProductRepository storeProductRepository,
                          StoreProductMapper storeProductMapper,
                          DiscountProductRepository discountProductRepository) {
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

    public List<ProductPriceHistoryDTO> getHistoryPrice(Optional<String> store, Optional<String> category, Optional<String> brand) {
        List<StoreProduct> storeProducts = storeProductRepository.findAll();
        List<DiscountProduct> discounts = discountProductRepository.findAll();

        Map<String, List<DiscountProduct>> discountMap = discounts.stream()
                .collect(Collectors.groupingBy(d -> d.getProduct().getId() + "|" + d.getStore()));

        return storeProducts.stream()
                .filter(sp -> store.map(s -> sp.getStore().equalsIgnoreCase(s)).orElse(true))
                .filter(sp -> category.map(c -> sp.getProduct().getProductCategory().equalsIgnoreCase(c)).orElse(true))
                .filter(sp -> brand.map(b -> sp.getProduct().getBrand().equalsIgnoreCase(b)).orElse(true))
                .collect(Collectors.groupingBy(StoreProduct::getProduct))
                .entrySet().stream()
                .map(entry -> {
                    Product product = entry.getKey();
                    List<StoreProduct> entries = entry.getValue();

                    List<ProductPriceHistoryDTO.PriceHistoryPoint> points = entries.stream()
                            .map(sp -> {
                                String key = sp.getProduct().getId() + "|" + sp.getStore();
                                List<DiscountProduct> applicableDiscounts = discountMap.getOrDefault(key, List.of());

                                Optional<DiscountProduct> matchingDiscount = applicableDiscounts.stream()
                                        .filter(d -> !sp.getDate().isBefore(d.getDiscountStartingDate())
                                                && !sp.getDate().isAfter(d.getDiscountEndingDate()))
                                        .findFirst();

                                boolean discounted = matchingDiscount.isPresent();
                                Double finalPrice = discounted
                                        ? sp.getPrice() * (1 - matchingDiscount.get().getPercentage() / 100.0)
                                        : sp.getPrice();

                                return new ProductPriceHistoryDTO.PriceHistoryPoint(sp.getDate(), finalPrice, discounted, sp.getStore());
                            })
                            .toList();

                    return new ProductPriceHistoryDTO(
                            product.getProductId(),
                            product.getProductName(),
                            product.getBrand(),
                            product.getProductCategory(),
                            points
                    );
                })
                .toList();
    }

    public RecommendationDTO getRecommendationsByProductName(String productName) {
        List<StoreProduct> products = storeProductRepository.findByProduct_ProductNameContainingIgnoreCase(productName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(productName);
        }

        StoreProduct base = products.stream()
                .min(Comparator.comparing(sp -> sp.getPrice() / sp.getProduct().getPackageQuantity()))
                .orElseThrow(() -> new ProductNotFoundException(productName));
        Product baseProduct = base.getProduct();
        String category = baseProduct.getProductCategory();
        String brand = baseProduct.getBrand();

        Double baseQuantity = baseProduct.getPackageQuantity();
        Double baseValuePerUnit = base.getPrice() / baseQuantity;

        RecommendationDTO.ProductDTO searchedDTO = new RecommendationDTO.ProductDTO(
                baseProduct.getProductName(),
                brand,
                base.getStore(),
                base.getPrice(),
                baseQuantity,
                baseProduct.getPackageUnit(),
                baseValuePerUnit
        );

        List<StoreProduct> similarProducts = storeProductRepository.findAll().stream()
                .filter(sp -> sp.getProduct().getBrand().equalsIgnoreCase(brand))
                .filter(sp -> sp.getProduct().getProductCategory().equalsIgnoreCase(category))
                .filter(sp -> !sp.getProduct().getProductName().equalsIgnoreCase(baseProduct.getProductName())) // exclude the searched one
                .toList();

        List<RecommendationDTO.ProductDTO> recommendations = similarProducts.stream()
                .map(sp -> {
                    Product p = sp.getProduct();
                    double quantity = p.getPackageQuantity();
                    double valuePerUnit = sp.getPrice() / quantity;
                    return new RecommendationDTO.ProductDTO(
                            p.getProductName(),
                            p.getBrand(),
                            sp.getStore(),
                            sp.getPrice(),
                            quantity,
                            p.getPackageUnit(),
                            valuePerUnit
                    );
                })
                .filter(dto -> dto.getValuePerUnit() < baseValuePerUnit)
                .sorted(Comparator.comparingDouble(RecommendationDTO.ProductDTO::getValuePerUnit))
                .collect(Collectors.toList());

        return new RecommendationDTO(searchedDTO, recommendations);
    }

    public PriceAlertCheckResponse checkPrice(PriceAlertCheckRequest request){
        List<StoreProduct> candidates = storeProductRepository.findByProduct_ProductNameContainingIgnoreCaseAndDate(
                request.getProductName(), request.getDate()
        );

        List<DiscountProduct> allDiscounts = discountProductRepository.findAll();

        Map<String, List<DiscountProduct>> discountMap = allDiscounts.stream()
                .collect(Collectors.groupingBy(d -> d.getProduct().getId() + "|" + d.getStore()));

        List<PriceAlertCheckResponse.MatchedStoreDTO> matches = new ArrayList<>();

        for (StoreProduct sp : candidates) {
            String key = sp.getProduct().getId() + "|" + sp.getStore();
            List<DiscountProduct> discounts = discountMap.getOrDefault(key, List.of());

            Optional<DiscountProduct> applicable = discounts.stream()
                    .filter(d -> !request.getDate().isBefore(d.getDiscountStartingDate())
                            && !request.getDate().isAfter(d.getDiscountEndingDate()))
                    .findFirst();

            double finalPrice = sp.getPrice();
            boolean discounted = false;

            if (applicable.isPresent()) {
                discounted = true;
                finalPrice = finalPrice * (1 - applicable.get().getPercentage() / 100.0);
            }

            if (finalPrice <= request.getTargetPrice()) {
                matches.add(new PriceAlertCheckResponse.MatchedStoreDTO(
                        sp.getStore(), finalPrice, discounted
                ));
            }
        }

        return new PriceAlertCheckResponse(!matches.isEmpty(), matches);
    }

}