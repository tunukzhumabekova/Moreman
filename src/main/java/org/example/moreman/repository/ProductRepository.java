package org.example.moreman.repository;

import com.agro.public_.tables.Products;
import com.agro.public_.tables.records.ProductsRecord;
import org.example.moreman.model.request.ProductRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.agro.public_.Tables.PRODUCTS;

@Repository
public class ProductRepository {
    private final DSLContext dsl;

    public ProductRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<ProductRecord> getAllProducts() {
        return dsl.selectFrom(PRODUCTS)
                .fetch()
                .map(record -> new ProductRecord(
                        record.get(PRODUCTS.NAME),
                        record.get(PRODUCTS.IMAGE),
                        record.get(PRODUCTS.PRICE),
                        record.get(PRODUCTS.QUANTITY),
                        record.get(PRODUCTS.DESCRIPTION),
                        record.get(PRODUCTS.POPULAR),
                        record.get(PRODUCTS.CATEGORY_ID)
                ));
    }


    public ProductRecord getProductById(Long id) {
        ProductsRecord result = dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(Math.toIntExact(id)))
                .fetchOne();

        if (result != null) {
            return new ProductRecord(
                    result.get(PRODUCTS.NAME),
                    result.get(PRODUCTS.IMAGE),
                    result.get(PRODUCTS.PRICE),
                    result.get(PRODUCTS.QUANTITY),
                    result.get(PRODUCTS.DESCRIPTION),
                    result.get(PRODUCTS.POPULAR),
                    result.get(PRODUCTS.CATEGORY_ID)
            );
        }

        throw new RuntimeException("Product not found with ID: " + id);
    }


    public ProductRecord createProduct(ProductRecord productRecord) {
        ProductsRecord result = dsl.insertInto(PRODUCTS)
                .set(PRODUCTS.NAME, productRecord.name())
                .set(PRODUCTS.IMAGE, productRecord.image())
                .set(PRODUCTS.PRICE, productRecord.price())
                .set(PRODUCTS.QUANTITY, productRecord.quantity())
                .set(PRODUCTS.DESCRIPTION, productRecord.description())
                .set(PRODUCTS.POPULAR, productRecord.isPopular())
                .set(PRODUCTS.CATEGORY_ID, productRecord.categoryId())
                .returning(PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.IMAGE, PRODUCTS.PRICE, PRODUCTS.QUANTITY, PRODUCTS.DESCRIPTION, PRODUCTS.POPULAR, PRODUCTS.CATEGORY_ID)
                .fetchOne();

        if (result != null) {
            return new ProductRecord(
                    result.getName(),
                    result.getImage(),
                    result.getPrice(),
                    result.getQuantity(),
                    result.getDescription(),
                    result.getPopular(),
                    result.getCategoryId()
            );
        }

        throw new RuntimeException("Failed to create product");
    }

    public void updateProduct(ProductRecord productsRecord,long id) {
        dsl.update(Products.PRODUCTS)
                .set(Products.PRODUCTS.NAME, productsRecord.name())
                .set(Products.PRODUCTS.PRICE, productsRecord.price())
                .set(Products.PRODUCTS.QUANTITY, productsRecord.quantity())
                .set(Products.PRODUCTS.DESCRIPTION, productsRecord.description())
                .set(Products.PRODUCTS.POPULAR, productsRecord.isPopular())
                .set(Products.PRODUCTS.CATEGORY_ID, productsRecord.categoryId())
                .where(Products.PRODUCTS.ID.eq(Math.toIntExact(id)))
                .execute();
    }

    public void deleteProduct(Long id) {
        dsl.deleteFrom(Products.PRODUCTS)
                .where(Products.PRODUCTS.ID.eq(Math.toIntExact(id)))
                .execute();
    }
    public List<ProductRecord> getProductsByCategoryId(int categoryId) {
        List<ProductsRecord> results = dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.CATEGORY_ID.eq(categoryId))
                .fetch();

        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductRecord> productRecords = results.stream()
                .map(result -> new ProductRecord(
                        result.getName(),
                        result.getImage(),
                        result.getPrice(),
                        result.getQuantity(),
                        result.getDescription(),
                        result.getPopular(),
                        result.getCategoryId()
                ))
                .collect(Collectors.toList());

        return productRecords;
    }

}

