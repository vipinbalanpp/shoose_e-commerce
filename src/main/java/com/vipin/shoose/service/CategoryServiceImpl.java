package com.vipin.shoose.service;

import com.vipin.shoose.dto.CategoryDto;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {

        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        Category category=new Category();
        category.setCategoryName(categoryDto.getCategoryName());
           categoryRepository.save(category);
    }

    @Override
    public Category getCategoryByCategoryName(String categoryName) {
        Category category=categoryRepository.findByCategoryName(categoryName);
        return category;
    }

    @Override
    public void changeCategoryName(CategoryDto categoryDto) {
        Category category=categoryRepository.findByCategoryName(categoryDto.getCategoryName());
    }
}
