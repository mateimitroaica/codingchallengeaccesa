package org.unibuc.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibuc.demo.entities.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductNameAndBrandAndPackageQuantityAndPackageUnit(String productName, String brand, double quantity, String unit);
}
