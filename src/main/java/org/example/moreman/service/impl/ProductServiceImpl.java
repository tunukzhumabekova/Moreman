package org.example.moreman.service.impl;

import org.example.moreman.exception.NotFoundException;
import org.example.moreman.model.request.ProductRecord;
import org.example.moreman.model.request.ProductResponse;
import org.example.moreman.repository.CategoryRepository;
import org.example.moreman.repository.ProductRepository;
import org.example.moreman.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
     return productRepository.getAllProducts();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productRepository.getProductById(id);
    }

    @Override
    public ProductResponse createProduct(ProductRecord productRecord) {
        if (!categoryRepository.existByCategory(productRecord.categoryId())) {
            throw new NotFoundException("Category with this ID is not found");
        }
        return productRepository.createProduct(productRecord);
    }


    @Override
    public void updateProduct(ProductRecord productRecord, long id) {
        productRepository.updateProduct(productRecord, id);
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.deleteProduct(id);
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(int categoryId) {
        return productRepository.getProductsByCategoryId(categoryId);
    }
}

