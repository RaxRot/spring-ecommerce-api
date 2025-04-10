package com.raxrot.sproject.service;

import com.raxrot.sproject.dto.CategoryDTO;
import com.raxrot.sproject.exception.APIException;
import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category already exists");
        }

        if (category.getCategoryName().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name cannot be blank");
        }else{
            Category savedCategory = categoryRepository.save(category);
            CategoryDTO dtoToReturn = modelMapper.map(savedCategory, CategoryDTO.class);
            return dtoToReturn;
        }
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        if (allCategories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No categories found");
        }

        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : allCategories) {
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public List<CategoryDTO> findAllCategoriesPageable(int page, int size,String sortBy,String sortDirection) {
        Sort sort=sortDirection.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Category> allCategories = categoryRepository.findAll(pageable);
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : allCategories) {
            CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
       Optional<Category> category = categoryRepository.findById(id);
       if (category.isPresent()) {
           CategoryDTO categoryDTO = modelMapper.map(category.get(), CategoryDTO.class);
           return categoryDTO;
       }else{
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
       }
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
        Category categoryFromDb = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        categoryFromDb.setCategoryName(categoryDTO.getCategoryName());

        Category updatedCategory = categoryRepository.save(categoryFromDb);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
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
