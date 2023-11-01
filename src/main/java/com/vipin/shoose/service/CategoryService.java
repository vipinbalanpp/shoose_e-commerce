package com.vipin.shoose.service;


import com.vipin.shoose.dto.CategoryDto;
import com.vipin.shoose.model.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategory();
    public void addCategory(CategoryDto categoryDto);

    Category getCategoryByCategoryName(String categoryName);

    void editCategory(CategoryDto categoryDto);


    Category getCategoryById(Long categoryId);

    void blockCategory(Long categoryId);

    void unBlockCategory(Long categoryId);
}
