package org.example.moreman.service.impl;


import org.example.moreman.model.request.CategoryRecord;
import org.example.moreman.repository.CategoryRepository;
import org.example.moreman.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryRecord> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public CategoryRecord getCategoryById(Long id) {
        return categoryRepository.getCategoryById(id);
    }

    @Override
    public CategoryRecord createCategory(String name, String image) {
       return categoryRepository.createCategory(name, image);
    }

    @Override
    public void updateCategory(Long id, String name, String image) {
        categoryRepository.updateCategory(id, name, image);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteCategory(id);
    }
}
