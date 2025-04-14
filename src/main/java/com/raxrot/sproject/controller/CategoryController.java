package com.raxrot.sproject.controller;

import com.raxrot.sproject.dto.CategoryDTO;
import com.raxrot.sproject.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Category Controller", description = "Category CRUD operations, pagination, and search")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category", responses = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided")
    })
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO createdCategory = categoryService.save(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.findAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Get paginated and sorted list of categories")
    @GetMapping("/public/categories/paged")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesByPagination(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam(name = "sortBy", defaultValue = "categoryName") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {
        List<CategoryDTO> pagedCategories = categoryService.findAllCategoriesPageable(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedCategories);
    }

    @Operation(summary = "Get category by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        CategoryDTO category = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Operation(summary = "Update category by ID")
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                      @Valid @RequestBody CategoryDTO category) {
        CategoryDTO updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}