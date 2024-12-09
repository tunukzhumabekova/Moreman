package org.example.moreman.service;

import org.example.moreman.model.request.ProductRecord;

import java.util.List;


public interface ProductService {
     List<ProductRecord> getAllProducts() ;
    ProductRecord getProductById(Long id);
    ProductRecord createProduct(ProductRecord productRecord);
    void updateProduct(ProductRecord productRecord,long id);
    void deleteProduct(long id);
     List<ProductRecord> getProductsByCategoryId(int categoryId) ;


}

