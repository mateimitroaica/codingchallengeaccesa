package org.unibuc.demo.utils;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.repositories.ProductRepository;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class ImportServiceTest {
    @Autowired
    private ImportService importService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @BeforeEach
    void resetDatabase() {
        storeProductRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void testImport_success() throws IOException {
        importService.importProducts("test_2025-05-01.csv");

        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());

        List<StoreProduct> storeProducts = storeProductRepository.findAll();
        assertEquals(2, storeProducts.size());

        StoreProduct milk = storeProducts.stream()
                .filter(p -> p.getProduct().getProductName().equals("test milk"))
                .findFirst().orElseThrow();

        assertEquals("test", milk.getStore());
        assertEquals(LocalDate.of(2025, 5, 1), milk.getDate());
        assertEquals(5.50, milk.getPrice());
    }

    @Test
    void testImport_fail() {
        FileNotFoundException ex = assertThrows(FileNotFoundException.class, () -> {
            importService.importProducts("missing_2025-01-01.csv");
        });

        assertEquals("File not found: /data/products/missing_2025-01-01.csv", ex.getMessage());
    }

    @Test
    void testImport_invalidFilenameFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            importService.importProducts("badfile.csv");
        });
    }

    @Test
    void testImport_malformedRow_logsErrorButContinues() throws IOException {
        importService.importProducts("malformed_2025-05-01.csv");

        assertEquals(1, productRepository.count());
    }

    @Test
    void testImport_duplicateProduct_savedOnce() throws IOException {
        importService.importProducts("duplicates_2025-05-01.csv");

        assertEquals(2, productRepository.count());

        assertEquals(3, storeProductRepository.count());
    }

}
