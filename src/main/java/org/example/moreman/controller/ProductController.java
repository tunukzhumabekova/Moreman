package org.example.moreman.controller;

import org.example.moreman.model.request.ProductRecord;
import org.example.moreman.model.request.ProductResponse;
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
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRecord productRecord) {
        ProductResponse createdProduct = productService.createProduct(productRecord);
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
    public List<ProductResponse> getProductsByCategoryId(int categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }
}

