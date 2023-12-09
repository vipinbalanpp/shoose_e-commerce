package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.CategoryDto;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public String categories(Model model){
        List<Category> allCategories=categoryService.getAllCategory();
        model.addAttribute("categories",allCategories);
        return "admin/categories";
    }

    @PostMapping("/add-category")
    public String addCategory(CategoryDto categoryDto,
                              RedirectAttributes redirectAttributes){
        if(categoryRepository.existsByCategoryName(categoryDto.getCategoryName())){
            redirectAttributes.addFlashAttribute("categoryExists","Category exists");
            return "redirect:/admin/categories";
        }
        categoryService.addCategory(categoryDto);
        return "redirect:/admin/categories";
    }
    @PostMapping("/edit-category")
    public String renameCategory( CategoryDto categoryDto,
                                  RedirectAttributes redirectAttributes,Model model){
        if(categoryRepository.existsByCategoryName(categoryDto.getCategoryName())){
            redirectAttributes.addFlashAttribute("categoryExists","Category exists");
            return "redirect:/admin/categories";
        }
        categoryService.editCategory(categoryDto);
        return "redirect:/admin/categories";
    }
    @PostMapping("/block-category/{categoryId}")
    public String blockCategory(@PathVariable("categoryId") Long categoryId
                                ,RedirectAttributes redirectAttributes){
        categoryService.blockCategory(categoryId);
        return "redirect:/admin/categories";
    }
    @PostMapping("/unBlock-category/{categoryId}")
    public String unBlockCategory(@PathVariable("categoryId") Long categoryId
                                 ,RedirectAttributes redirectAttributes){
        categoryService.unBlockCategory(categoryId);
        return "redirect:/admin/categories";
    }
    @PostMapping("/add-offer")
    public String addOffer(@RequestParam Long categoryId,
                           @RequestParam Integer offerPercentage,
                           @RequestParam LocalDate startDate,
                           @RequestParam LocalDate endDate,
                           RedirectAttributes redirectAttributes) {
        categoryService.addOffer(categoryId,offerPercentage,startDate,endDate);
        redirectAttributes.addFlashAttribute("offerAddingSuccess","Offer Added SuccessFully");


        return "redirect:/admin/categories"; // Redirect to the dashboard or any other page
    }
    @PostMapping("/edit-offer")
    public String editOffer(@RequestParam Long categoryId,
                           @RequestParam Integer offerPercentage,
                           @RequestParam LocalDate startDate,
                           @RequestParam LocalDate endDate,
                           RedirectAttributes redirectAttributes) {
        categoryService.editOffer(categoryId,offerPercentage,endDate);
        redirectAttributes.addFlashAttribute("offerEditingSuccess","Offer Added SuccessFully");


        return "redirect:/admin/categories"; // Redirect to the dashboard or any other page
    }


}
