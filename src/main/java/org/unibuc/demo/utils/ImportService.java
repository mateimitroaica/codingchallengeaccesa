package org.unibuc.demo.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.unibuc.demo.entities.DiscountProduct;
import org.unibuc.demo.entities.Product;
import org.unibuc.demo.entities.StoreProduct;
import org.unibuc.demo.repositories.DiscountProductRepository;
import org.unibuc.demo.repositories.ProductRepository;
import org.unibuc.demo.repositories.StoreProductRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ImportService {
    private final ProductRepository productRepository;
    private final StoreProductRepository storeProductRepository;
    private final DiscountProductRepository discountProductRepository;

    public ImportService(ProductRepository productRepository, StoreProductRepository storeProductRepository, DiscountProductRepository discountProductRepository) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
        this.discountProductRepository = discountProductRepository;
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

    public void importDiscounts(String filename) throws IOException {
        String[] arr = filename.replace(".csv", "").split("_");

        if (arr.length != 3) {
            throw new IllegalArgumentException("Invalid filename format");
        }
        String storeName = arr[0];
        LocalDate date = LocalDate.parse(arr[2]);

        InputStream inputStream = getClass().getResourceAsStream("/data/discounts/" + filename);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: /data/discounts/" + filename);
        }

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build())
        {
            String[] line;
            reader.readNext();

            while ((line = reader.readNext()) != null) {
                try {
                    if (line.length < 9) {
                        System.out.println("Malformed row: " + Arrays.toString(line));
                        continue;
                    }
                    String productName = line[1];
                    String brand = line[2];
                    double quantity = Double.parseDouble(line[3]);
                    String unit = line[4];
                    LocalDate fromDate = LocalDate.parse(line[6]);
                    LocalDate toDate = LocalDate.parse(line[7]);
                    double percentage = Double.parseDouble(line[8]);

                    Optional<Product> productOpt = productRepository.findByProductNameAndBrandAndPackageQuantityAndPackageUnit(
                            productName, brand, quantity, unit);

                    if (productOpt.isEmpty()) {
                        System.out.println("Product not found: " + productName);
                        continue;
                    }

                    Product product = productOpt.get();

                    Boolean exists = discountProductRepository.existsByProductAndDiscountStartingDateAndDiscountEndingDate(
                            product, fromDate, toDate);

                    if (exists) {
                        System.out.println("Duplicate discount skipped: " + productName);
                        continue;
                    }

                    DiscountProduct discount = new DiscountProduct();
                    discount.setProduct(product);
                    discount.setDate(date);
                    discount.setDiscountStartingDate(fromDate);
                    discount.setDiscountEndingDate(toDate);
                    discount.setPercentage(percentage);
                    discount.setStore(storeName);

                    discountProductRepository.save(discount);
                } catch (Exception rowError) {
                    System.err.println("Failed to process row: " + Arrays.toString(line));
                }
            }

        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV format error in file: " + filename, e);
        }
    }

    public boolean isDatabaseEmpty() {
        return productRepository.count() == 0 && discountProductRepository.count() == 0;
    }


}
