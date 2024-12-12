package org.example.moreman.controller;



import org.example.moreman.model.request.CategoryRecord;
import org.example.moreman.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryRecord>> getAllCategories() {
        List<CategoryRecord> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryRecord> getCategoryById(@PathVariable Integer id) {
        CategoryRecord category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryRecord> createCategory(@RequestParam String name, @RequestParam String image) {
        CategoryRecord categoryRecord = categoryService.createCategory(name, image);
        return ResponseEntity.ok(categoryRecord);
    }


    // Update an existing category
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Integer id,
                                               @RequestParam String name,
                                               @RequestParam String image) {
        categoryService.updateCategory(id, name, image);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
