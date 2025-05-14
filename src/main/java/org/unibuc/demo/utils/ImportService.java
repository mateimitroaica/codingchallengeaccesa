package org.unibuc.demo.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.repositories.ProductRepository;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class ImportService {
    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;

    public ImportService(ProductRepository productRepository, StoreProductRepository storeProductRepository) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
    }

    public void importProducts(String filename) throws IOException {
        String[] arr = filename.replace(".csv", "").split("_");

        if (arr.length != 2) {
            throw new IllegalArgumentException("Invalid filename format");
        }
        String storeName = arr[0];
        LocalDate date = LocalDate.parse(arr[1]);

        InputStream inputStream = getClass().getResourceAsStream("/data/products/" + filename);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: /data/products/" + filename);
        }

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) // tell OpenCSV to use ";" as a delimiter
        {
            String[] line;
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                try {
                    if (line.length < 8) {
                        System.out.println("Malformed product: " + Arrays.toString(line));
                        continue;
                    }
                    String productId = line[0];
                    String productName = line[1];
                    String category = line[2];
                    String brand = line[3];
                    double quantity = Double.parseDouble(line[4]);
                    String unit = line[5];
                    double price = Double.parseDouble(line[6]);
                    String currency = line[7];

                    Product product = productRepository
                            .findByProductNameAndBrandAndPackageQuantityAndPackageUnit(productName, brand, quantity, unit)
                            .orElseGet(() -> {
                                Product newProd = new Product();
                                newProd.setProductId(productId);
                                newProd.setProductName(productName);
                                newProd.setProductCategory(category);
                                newProd.setBrand(brand);
                                newProd.setPackageQuantity(quantity);
                                newProd.setPackageUnit(unit);
                                return productRepository.save(newProd);
                            });

                    if (!storeProductRepository.existsByProductAndStoreAndDateAndPrice(product, storeName, date, price)) {
                        StoreProduct storeProduct = new StoreProduct();
                        storeProduct.setProduct(product);
                        storeProduct.setStore(storeName);
                        storeProduct.setDate(date);
                        storeProduct.setPrice(price);
                        storeProduct.setCurrency(currency);

                        storeProductRepository.save(storeProduct);
                    } else {
                        System.out.println("Duplicate product: " + product.getProductName());
                    }
                } catch (Exception rowError) {
                    System.err.println("Failed to process row: " + Arrays.toString(line));
                }
            }

        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV format error in file: " + filename, e);
        }
    }


}
