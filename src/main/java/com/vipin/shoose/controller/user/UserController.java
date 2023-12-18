package com.vipin.shoose.controller.user;
import com.vipin.shoose.dto.ProductToShopPage;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.service.CategoryService;
import com.vipin.shoose.service.ProductImageService;
import com.vipin.shoose.service.ProductService;
import com.vipin.shoose.service.VariantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
@Slf4j
public class UserController {
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;


    @GetMapping("/user/home")
    public String home(Model model) {
        model.addAttribute("categories",categoryService.getAllActiveCategory());
        model.addAttribute("productList",  productService.getProductToHome());
        return "user/home";
    }

    @GetMapping("/shop")
    public String shop(Model model){

        model.addAttribute("productList",  productService.getProductToHome());
        return "user/shop";
    }
    @GetMapping("/product-details")
    public String productDetails(@RequestParam("productId") Long productId,Model model){
        Product product=productService.getProductById(productId);
        List<String>colors=    productService.getProductColors(product);
        model.addAttribute("product",product);
        model.addAttribute("colors",colors);
        return "user/product-details";
    }
    @GetMapping("/get-product-images")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductImages(
            @RequestParam("productId") Long productId,
            @RequestParam("color") String selectedColor){
        List<ProductImage> images = productImageService.getProductImagesByProductColor(productId, selectedColor);
       List<Long> sizes= productService.getProductSizesForColor(productId,selectedColor);
        List<String> img=new ArrayList<>();
        for(ProductImage image:images){
            img.add(image.getImage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("images", img);
        response.put("sizes",sizes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
