package org.example.moreman.service;




import org.example.moreman.model.request.CategoryRecord;

import java.util.List;

public interface CategoryService {
    List<CategoryRecord> getAllCategories();

    CategoryRecord getCategoryById(Integer id);

    CategoryRecord createCategory(String name, String image);

    void updateCategory(Integer id, String name, String image);

    void deleteCategory(Integer id);
}
