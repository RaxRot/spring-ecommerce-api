package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO categoryDTO);
    List<CategoryDTO> findAllCategories();
    List<CategoryDTO>findAllCategoriesPageable(int page, int size,String sortBy,String sortDirection);
    CategoryDTO findCategoryById(Long id);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);
    void deleteCategoryById(Long id);
}
