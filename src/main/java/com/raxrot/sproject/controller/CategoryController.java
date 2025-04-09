package com.raxrot.sproject.controller;

import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/public/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.save(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("public/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId ,@RequestBody Category category) {
        Category updatedCategory =categoryService.updateCategory(category,categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
