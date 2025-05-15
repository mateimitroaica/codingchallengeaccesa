package org.unibuc.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibuc.demo.entities.DiscountProduct;
import org.unibuc.demo.entities.Product;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {
    Boolean existsByProductAndDiscountStartingDateAndDiscountEndingDate(
            Product product, LocalDate start, LocalDate end
    );

    List<DiscountProduct> findByDiscountStartingDateLessThanEqualAndDiscountEndingDateGreaterThanEqual(
            LocalDate start, LocalDate end
    );

    List<DiscountProduct> findByDiscountStartingDateBetween(LocalDate from, LocalDate to);
}
