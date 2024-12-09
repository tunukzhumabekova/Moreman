package org.example.moreman.service;




import org.example.moreman.model.request.CategoryRecord;

import java.util.List;

public interface CategoryService {
    List<CategoryRecord> getAllCategories();

    CategoryRecord getCategoryById(Long id);

    CategoryRecord createCategory(String name, String image);

    public void updateCategory(Long id, String name, String image);

    public void deleteCategory(Long id);
}
