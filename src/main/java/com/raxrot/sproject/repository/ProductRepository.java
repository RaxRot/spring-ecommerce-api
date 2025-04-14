package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.Category;
import com.raxrot.sproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryOrderByPrice(Category category);

    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
