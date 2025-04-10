package com.raxrot.sproject.service;

import com.raxrot.sproject.exception.APIException;
import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category already exists");
        }

        if (category.getCategoryName().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be blank");
        }else{
            Category savedCategory = categoryRepository.save(category);
            return savedCategory;
        }
    }

    @Override
    public List<Category> findAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if (allCategories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No categories found");
        }
        return allCategories;
    }

    @Override
    public Category findCategoryById(Long id) {
       Optional<Category> category = categoryRepository.findById(id);
       if (category.isPresent()) {
           return category.get();
       }else{
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
       }
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category categoryFromDb = findCategoryById(id);
        categoryFromDb.setCategoryName(category.getCategoryName());
        return categoryRepository.save(categoryFromDb);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}
