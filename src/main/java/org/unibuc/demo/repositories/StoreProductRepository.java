package org.unibuc.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    Boolean existsByProductAndStoreAndDateAndPrice(Product product, String store, LocalDate date, double price);
    List<StoreProduct> findByProduct_ProductNameAndDate(String productName, LocalDate date);
    Optional<StoreProduct> findByProductAndStoreAndDate(Product product, String store, LocalDate date);
}
