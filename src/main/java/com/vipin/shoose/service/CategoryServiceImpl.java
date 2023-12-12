package com.vipin.shoose.service;

import com.vipin.shoose.dto.CategoryDto;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.util.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageUpload imageUpload;

    @Override
    public List<Category> getAllCategory() {

        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        try {
            Category category=new Category();
            category.setCategoryName(categoryDto.getCategoryName());
            category.setDescription(categoryDto.getDescription());
            category.setImage(imageUpload.saveImage(categoryDto.getCategoryImage()));
            category.setEnabled(true);
            categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public Category getCategoryByCategoryName(String categoryName) {
        try {
            Category category=categoryRepository.findByCategoryName(categoryName);
            return category;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public void editCategory(CategoryDto categoryDto) {
        try {
            Category category=categoryRepository.findByCategoryId(categoryDto.getCategoryId());
            if(categoryDto.getCategoryName()!=""){
                category.setCategoryName(categoryDto.getCategoryName());
            }if(categoryDto.getDescription()!=""){
                category.setDescription(categoryDto.getDescription());
            }
            categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public Category getCategoryById(Long categoryId) {
        try {
            return categoryRepository.findByCategoryId(categoryId);
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public void blockCategory(Long categoryId) {
        try {
            Category category=categoryRepository.findByCategoryId(categoryId);
            category.setEnabled(false);
            categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }

    @Override
    public void unBlockCategory(Long categoryId) {
        try {
            Category category=categoryRepository.findByCategoryId(categoryId);
            category.setEnabled(true);
            categoryRepository.save(category);
        }catch (Exception e){
            throw new RuntimeException("An error occurred");
        }

    }

    @Override
    public List<Category> getCategoriesForEditingProduct(Long productId) {
        try {
            List<Category>categories=categoryRepository.findAll();
            categories.remove(productRepository.findByProductId(productId).getCategory());
            return categories;
        }catch (Exception e){
            throw  new RuntimeException();
        }

    }

    @Override
    public void addOffer(Long categoryId, Integer offerPercentage, LocalDate startDate, LocalDate endDate) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setIsHavingOffer(true);
        category.setOfferInPercentage(offerPercentage);
        category.setStartDate(startDate);
        category.setExpiryDate(endDate);
        categoryRepository.save(category);
        productService.setOfferToProducts(categoryId);
    }
    @Scheduled(cron = "59 59 23 * * *")
    public void expireOffers() {
        try {
            LocalDate currentDate = LocalDate.now();
            List<Category> categoriesWithExpiredOffers = categoryRepository.findByExpiryDateLessThanAndIsHavingOfferTrue(currentDate);
            System.out.println("cronJob working");
            for (Category category : categoriesWithExpiredOffers) {
                category.setIsHavingOffer(false);
            }
            categoryRepository.saveAll(categoriesWithExpiredOffers);
            productService.updateProductsAfterOfferExpiration(categoriesWithExpiredOffers);
        }catch (Exception e){
            throw new RuntimeException();
        }

    }
    @Override
    public void editOffer(Long categoryId, Integer offerPercentage, LocalDate endDate) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        if(offerPercentage!=null){
            category.setOfferInPercentage(offerPercentage);
        }
        if(endDate!=null){
            category.setExpiryDate(endDate);
        }categoryRepository.save(category);
        productService.setOfferToProducts(categoryId);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        return categoryRepository.findByEnabled();
    }

    @Override
    public void removeOffer(Long categoryId) {
        try {
            if(categoryRepository.findByCategoryId(categoryId)!=null){
                Category category=categoryRepository.findByCategoryId(categoryId);
                category.setIsHavingOffer(false);
                List<Product>products=productRepository.findByCategory(category);
                products.forEach(product ->
                {
                    product.setIsCategoryHavingOffer(false);
                    productRepository.save(product);
                });
                categoryRepository.save(category);
            }
        }catch (Exception e){
            throw new RuntimeException();
        }

    }


}
