package org.example.moreman.controller;

import org.example.moreman.model.request.ProductRecord;
import org.example.moreman.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductRecord>> getAllProducts() {
        List<ProductRecord> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRecord> getProductById(@PathVariable Long id) {
        ProductRecord product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductRecord> createProduct(@RequestBody ProductRecord productRecord) {
        ProductRecord createdProduct = productService.createProduct(productRecord);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductRecord productRequest) {
        productService.updateProduct(productRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/getByCategoryId")
    public List<ProductRecord> getProductsByCategoryId(int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
}

