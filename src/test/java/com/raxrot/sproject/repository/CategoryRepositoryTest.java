package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @DisplayName("Test add category to Db")
    @Test
    void testAddCategory() {
        Category category = new Category();
        category.setCategoryName("TestFromTest");
        Category savedCategory = repository.save(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isGreaterThan(0);
        assertThat(savedCategory.getCategoryName()).isEqualTo(category.getCategoryName());
    }

    @DisplayName("Test get all categories from Db")
    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        category1.setCategoryName("TestFromTest");
        Category category2 = new Category();
        category2.setCategoryName("TestFromTest2");
        repository.save(category1);
        repository.save(category2);
        List<Category> categories = repository.findAll();

        assertThat(categories).isNotNull();
        assertThat(categories.size()).isEqualTo(2);
    }

   @DisplayName("Test update category")
    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setCategoryName("TestFromTest");
        repository.save(category);

        Category categoryfound = repository.findById(category.getId()).get();
        categoryfound.setCategoryName("Work");
        repository.save(categoryfound);

        assertThat(repository.findById(category.getId())).isNotNull();
        assertThat(repository.findById(category.getId()).get().getCategoryName()).isEqualTo("Work");
   }

   @DisplayName("Test delete category")
    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setCategoryName("TestFromTest");
        Category savedCategory = repository.save(category);
        repository.deleteById(savedCategory.getId());
        assertThat(repository.findById(savedCategory.getId())).isEmpty();
   }
}