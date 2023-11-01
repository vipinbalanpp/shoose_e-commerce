package com.vipin.shoose.repository;

import com.vipin.shoose.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
      public Category findByCategoryId(Long categoryId);

      public Category findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
