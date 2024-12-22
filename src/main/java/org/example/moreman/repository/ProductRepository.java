package org.example.moreman.repository;

import com.agro.public_.tables.Products;
import com.agro.public_.tables.records.ProductsRecord;
import org.example.moreman.exception.NotFoundException;
import org.example.moreman.model.request.ProductRecord;
import org.example.moreman.model.request.ProductResponse;
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

    public List<ProductResponse> getAllProducts() {
        return dsl.selectFrom(PRODUCTS)
                .fetch()
                .map(record -> new ProductResponse(
                        record.get(PRODUCTS.ID),
                        record.get(PRODUCTS.NAME),
                        record.get(PRODUCTS.IMAGE),
                        record.get(PRODUCTS.PRICE),
                        record.get(PRODUCTS.QUANTITY),
                        record.get(PRODUCTS.DESCRIPTION),
                        record.get(PRODUCTS.POPULAR),
                        record.get(PRODUCTS.CATEGORY_ID),
                        record.getSale()
                ));
    }


    public ProductResponse getProductById(Long id) {
        ProductsRecord result = dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.ID.eq(Math.toIntExact(id)))
                .fetchOne();

        if (result != null) {
            return new ProductResponse(
                    result.get(PRODUCTS.ID),
                    result.get(PRODUCTS.NAME),
                    result.get(PRODUCTS.IMAGE),
                    result.get(PRODUCTS.PRICE),
                    result.get(PRODUCTS.QUANTITY),
                    result.get(PRODUCTS.DESCRIPTION),
                    result.get(PRODUCTS.POPULAR),
                    result.get(PRODUCTS.CATEGORY_ID),
                    result.getSale()
            );
        }

        throw new NotFoundException("Product not found with ID: " + id);
    }


    public ProductResponse createProduct(ProductRecord productRecord) {
        ProductsRecord result = dsl.insertInto(PRODUCTS)
                .set(PRODUCTS.NAME, productRecord.name())
                .set(PRODUCTS.IMAGE, productRecord.image())
                .set(PRODUCTS.PRICE, productRecord.price())
                .set(PRODUCTS.QUANTITY, productRecord.quantity())
                .set(PRODUCTS.DESCRIPTION, productRecord.description())
                .set(PRODUCTS.POPULAR, productRecord.isPopular())
                .set(PRODUCTS.CATEGORY_ID, productRecord.categoryId())
                .set(PRODUCTS.SALE,productRecord.sale())
                .returning(PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.IMAGE, PRODUCTS.PRICE, PRODUCTS.QUANTITY, PRODUCTS.DESCRIPTION, PRODUCTS.POPULAR, PRODUCTS.CATEGORY_ID,PRODUCTS.SALE)
                .fetchOne();

        if (result != null) {
            return new ProductResponse(
                    result.getId(),
                    result.getName(),
                    result.getImage(),
                    result.getPrice(),
                    result.getQuantity(),
                    result.getDescription(),
                    result.getPopular(),
                    result.getCategoryId(),
                    result.getSale()
            );
        }

        throw new NotFoundException("Failed to create product");
    }

    public void updateProduct(ProductRecord productsRecord,long id) {
        dsl.update(Products.PRODUCTS)
                .set(Products.PRODUCTS.NAME, productsRecord.name())
                .set(Products.PRODUCTS.IMAGE, productsRecord.image())
                .set(Products.PRODUCTS.PRICE, productsRecord.price())
                .set(Products.PRODUCTS.QUANTITY, productsRecord.quantity())
                .set(Products.PRODUCTS.DESCRIPTION, productsRecord.description())
                .set(Products.PRODUCTS.POPULAR, productsRecord.isPopular())
                .set(Products.PRODUCTS.CATEGORY_ID, productsRecord.categoryId())
                .set(Products.PRODUCTS.SALE, productsRecord.sale())
                .where(Products.PRODUCTS.ID.eq(Math.toIntExact(id)))
                .execute();
    }

    public void deleteProduct(Long id) {
        dsl.deleteFrom(Products.PRODUCTS)
                .where(Products.PRODUCTS.ID.eq(Math.toIntExact(id)))
                .execute();
    }
    public List<ProductResponse> getProductsByCategoryId(int categoryId) {
        List<ProductsRecord> results = dsl.selectFrom(PRODUCTS)
                .where(PRODUCTS.CATEGORY_ID.eq(categoryId))
                .fetch();

        if (results.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductResponse> productRecords = results.stream()
                .map(result -> new ProductResponse(
                        result.getId(),
                        result.getName(),
                        result.getImage(),
                        result.getPrice(),
                        result.getQuantity(),
                        result.getDescription(),
                        result.getPopular(),
                        result.getCategoryId(),
                        result.getSale()
                ))
                .collect(Collectors.toList());

        return productRecords;
    }

}

