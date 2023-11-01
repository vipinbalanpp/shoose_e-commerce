package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.CategoryDto;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/admin/categories")
    public String categories(Model model){
        List<Category> allCategories=categoryService.getAllCategory();
        model.addAttribute("categories",allCategories);
        return "admin/categories";
    }

    @PostMapping("admin/add-category")
    public String addCategory(CategoryDto categoryDto,
                              RedirectAttributes redirectAttributes){
        if(categoryRepository.existsByCategoryName(categoryDto.getCategoryName())){
            redirectAttributes.addFlashAttribute("categoryExists","Category exists");
            return "redirect:/admin/categories";
        }
        categoryService.addCategory(categoryDto);
        return "redirect:/admin/categories";
    }
    @PostMapping("/admin/edit-category")
    public String renameCategory( CategoryDto categoryDto,
                                  RedirectAttributes redirectAttributes,Model model){
        if(categoryRepository.existsByCategoryName(categoryDto.getCategoryName())){
            redirectAttributes.addFlashAttribute("categoryExists","Category exists");
            return "redirect:/admin/categories";
        }
        categoryService.editCategory(categoryDto);
        return "redirect:/admin/categories";
    }
    @PostMapping("admin/block-category/{categoryId}")
    public String blockCategory(@PathVariable("categoryId") Long categoryId
                                ,RedirectAttributes redirectAttributes){
        categoryService.blockCategory(categoryId);
        return "redirect:/admin/categories";
    }
    @PostMapping("admin/unBlock-category/{categoryId}")
    public String unBlockCategory(@PathVariable("categoryId") Long categoryId
                                 ,RedirectAttributes redirectAttributes){
        categoryService.unBlockCategory(categoryId);
        return "redirect:/admin/categories";
    }

}
