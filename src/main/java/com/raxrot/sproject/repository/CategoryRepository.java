package com.raxrot.sproject.repository;

import com.raxrot.sproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
