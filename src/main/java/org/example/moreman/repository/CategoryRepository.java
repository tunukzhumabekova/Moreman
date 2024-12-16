package org.example.moreman.repository;

import com.agro.public_.tables.Category;
import org.example.moreman.model.request.CategoryRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.agro.public_.Tables.CATEGORY;

@Repository
public class CategoryRepository {
    private final DSLContext dsl;

    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CategoryRecord> getAllCategories() {
        return dsl.selectFrom(Category.CATEGORY).fetchInto(CategoryRecord.class);
    }

    public CategoryRecord getCategoryById(Integer id) {
        return dsl.selectFrom(Category.CATEGORY)
                .where(Category.CATEGORY.ID.eq(Math.toIntExact(id)))
                .fetchOneInto(CategoryRecord.class);
    }

    public CategoryRecord createCategory(String name, String image) {
        CategoryRecord result = dsl.insertInto(CATEGORY)
                .set(CATEGORY.NAME, name)
                .set(CATEGORY.IMAGE, image)
                .returning(CATEGORY.ID, CATEGORY.NAME, CATEGORY.IMAGE)
                .fetchOneInto(CategoryRecord.class);

        if (result != null) {
            return new CategoryRecord(
                    result.id(),
                    result.image(),
                    result.name()
                    );
        }

        throw new RuntimeException("Failed to create category");
    }
    public void updateCategory(Integer id, String name, String image) {
        dsl.update(Category.CATEGORY)
                .set(Category.CATEGORY.NAME, name)
                .set(Category.CATEGORY.IMAGE, image)
                .where(Category.CATEGORY.ID.eq(Math.toIntExact(id)))
                .execute();
    }

    public void deleteCategory(Integer id) {
        dsl.deleteFrom(Category.CATEGORY)
                .where(Category.CATEGORY.ID.eq(Math.toIntExact(id)))
                .execute();
    }
    public boolean existByCategory(Integer categoryId) {
        return dsl.fetchExists(
                dsl.selectFrom(Category.CATEGORY)
                        .where(Category.CATEGORY.ID.eq(categoryId))
        );
    }
}

