package com.vipin.shoose.repository;

import com.vipin.shoose.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
      public Category findByCategoryId(Long categoryId);

      public Category findByCategoryName(String categoryName);
}
