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
        category.setDescription(categoryDto.getDescription());
        category.setEnabled(true);
           categoryRepository.save(category);
    }

    @Override
    public Category getCategoryByCategoryName(String categoryName) {
        Category category=categoryRepository.findByCategoryName(categoryName);
        return category;
    }

    @Override
    public void editCategory(CategoryDto categoryDto) {
        Category category=categoryRepository.findByCategoryId(categoryDto.getCategoryId());
        if(categoryDto.getCategoryName()!=""){
            category.setCategoryName(categoryDto.getCategoryName());
        }if(categoryDto.getDescription()!=""){
            category.setDescription(categoryDto.getDescription());
        }
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public void blockCategory(Long categoryId) {
        Category category=categoryRepository.findByCategoryId(categoryId);
        category.setEnabled(false);
        categoryRepository.save(category);
    }

    @Override
    public void unBlockCategory(Long categoryId) {
        Category category=categoryRepository.findByCategoryId(categoryId);
        category.setEnabled(true);
        categoryRepository.save(category);
    }


}
