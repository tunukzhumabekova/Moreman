package org.example.moreman.service;

import org.example.moreman.model.request.ProductRecord;
import org.example.moreman.model.request.ProductResponse;

import java.util.List;


public interface ProductService {
     List<ProductResponse> getAllProducts() ;
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductRecord productRecord);
    void updateProduct(ProductRecord productRecord,long id);
    void deleteProduct(long id);
     List<ProductResponse> getProductsByCategoryId(int categoryId) ;


}

