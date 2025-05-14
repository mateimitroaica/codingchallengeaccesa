package org.unibuc.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;

import java.time.LocalDate;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    Boolean existsByProductAndStoreAndDateAndPrice(Product product, String store, LocalDate date, double price);
}
