package org.unibuc.demo.init;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.unibuc.demo.utils.ImportService;

import java.io.IOException;

@Component
public class DataInit {

    private final ImportService importService;

    public DataInit(ImportService importService) {
        this.importService = importService;
    }

    @PostConstruct
    public void init() {
        try {
            // products import:
            importService.importProducts("kaufland_2025-05-01.csv");
            importService.importProducts("kaufland_2025-05-08.csv");
            importService.importProducts("lidl_2025-05-01.csv");
            importService.importProducts("lidl_2025-05-08.csv");
            importService.importProducts("profi_2025-05-01.csv");
            importService.importProducts("profi_2025-05-08.csv");

            // discounts import:
            importService.importDiscounts("kaufland_discounts_2025-05-01.csv");
            importService.importDiscounts("kaufland_discounts_2025-05-08.csv");
            importService.importDiscounts("lidl_discounts_2025-05-01.csv");
            importService.importDiscounts("lidl_discounts_2025-05-08.csv");
            importService.importDiscounts("profi_discounts_2025-05-01.csv");
            importService.importDiscounts("profi_discounts_2025-05-08.csv");

        } catch (IOException e) {
            System.err.println(" Import failed: " + e.getMessage());
        }

    }

}
