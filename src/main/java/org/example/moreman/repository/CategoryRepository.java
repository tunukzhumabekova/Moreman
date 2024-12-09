package org.example.moreman.service.impl;

import com.agro.public_.tables.Category;
import com.agro.public_.tables.records.CategoryRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final DSLContext dsl;

    public CategoryService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CategoryRecord> getAllCategories() {
        return dsl.selectFrom(Category.CATEGORY).fetchInto(CategoryRecord.class);
    }

    public CategoryRecord getCategoryById(Long id) {
        return dsl.selectFrom(Category.CATEGORY)
                .where(Category.CATEGORY.ID.eq(Math.toIntExact(id)))
                .fetchOne();
    }

    public void createCategory(String name, String image) {
        dsl.insertInto(Category.CATEGORY)
                .set(Category.CATEGORY.NAME, name)
                .set(Category.CATEGORY.IMAGE, image)
                .execute();
    }

    public void updateCategory(Long id, String name, String image) {
        dsl.update(Category.CATEGORY)
                .set(Category.CATEGORY.NAME, name)
                .set(Category.CATEGORY.IMAGE, image)
                .where(Category.CATEGORY.ID.eq(id))
                .execute();
    }

    public void deleteCategory(Long id) {
        dsl.deleteFrom(Category.CATEGORY)
                .where(Category.CATEGORY.ID.eq(id))
                .execute();
    }
}

