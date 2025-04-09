package com.raxrot.sproject.service;

import com.raxrot.sproject.model.Category;

import java.util.List;

public interface CategoryService {
    Category save(Category category);
    List<Category> findAllCategories();
    Category findCategoryById(Long id);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}
