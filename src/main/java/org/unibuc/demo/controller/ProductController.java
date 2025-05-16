package org.unibuc.demo.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibuc.demo.dto.*;
import org.unibuc.demo.service.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/best-deals")
    public ResponseEntity<BasketResponseDTO> getBestDeals(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                          @RequestBody List<String> products) {
        BasketResponseDTO responseDTO = productService.basketMonitoring(date, products);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/top-discounts")
    public ResponseEntity<List<DiscountProductDTO>> getBestCurrentDiscounts(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DiscountProductDTO> discounts = productService.getBestCurrentDiscounts(date);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/new-discounts")
    public ResponseEntity<List<DiscountProductDTO>> getLatestDiscounts(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DiscountProductDTO> discounts = productService.getNewDiscounts(date);
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/price-history")
    public ResponseEntity<List<ProductPriceHistoryDTO>> getDynamicPriceHistory(
            @RequestParam(required = false) Optional<String> store,
            @RequestParam(required = false) Optional<String> category,
            @RequestParam(required = false) Optional<String> brand
            ) {
        List<ProductPriceHistoryDTO> result = productService.getHistoryPrice(store, category, brand);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationDTO> getRecommendations(@RequestParam String productName) {
        RecommendationDTO recommendation = productService.getRecommendationsByProductName(productName);
        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("/price-alert")
    public ResponseEntity<PriceAlertCheckResponse> priceAlert(@RequestBody PriceAlertCheckRequest request) {
        PriceAlertCheckResponse response = productService.checkPrice(request);
        return ResponseEntity.ok(response);
    }

}