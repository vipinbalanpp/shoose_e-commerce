package com.vipin.shoose.controller.user;
import com.vipin.shoose.dto.ProductToShopPage;
import com.vipin.shoose.dto.UpdateImagesAndSizesRequest;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.service.ProductImageService;
import com.vipin.shoose.service.ProductService;
import com.vipin.shoose.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;
    @Autowired
    VariantService variantService;


    @GetMapping("/user/home")
    public String home(Model model) {
        List<Product> products = productService.getAllActiveProducts();
        if (products != null) {
            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                Category category = categoryRepository.findByCategoryId(product.getCategory().getCategoryId());
                if (!category.isEnabled()) {
                    iterator.remove();
                }
            }
        }

        Set<ProductToShopPage> productList = new HashSet<>();
        List<ProductImage> productImages = productImageService.productImageList();
        for (Product product : products) {
            for (ProductImage productImage : productImages) {
                if (Objects.equals(product.getProductId(), productImage.getProduct().getProductId())) {
                    ProductToShopPage productToShopPage = new ProductToShopPage();
                    productToShopPage.setProductId(product.getProductId());
                    productToShopPage.setPrice(String.valueOf(product.getPrice()));
                    productToShopPage.setProductName(product.getProductName());
                    productToShopPage.setBrand(product.getBrand());
                    productToShopPage.setProductImage(productImage.getImage());
                    productList.add(productToShopPage);
                    break;
                }
            }
        }

        model.addAttribute("productList", productList);
        return "user/home";
    }

    @GetMapping("/shop")
    public String shop(Model model){
        List<Product> products = productService.getAllActiveProducts();
        if (products != null) {
            Iterator<Product> iterator = products.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                Category category = categoryRepository.findByCategoryId(product.getCategory().getCategoryId());
                if (!category.isEnabled()) {
                    iterator.remove();
                }
            }
        }

        Set<ProductToShopPage> productList = new HashSet<>();
        List<ProductImage> productImages = productImageService.productImageList();

        for (Product product : products) {
            for (ProductImage productImage : productImages) {
                if (product.getProductId() == productImage.getProduct().getProductId()) {
                    ProductToShopPage productToShopPage = new ProductToShopPage();
                    productToShopPage.setProductId(product.getProductId());
                    productToShopPage.setPrice(String.valueOf(product.getPrice()));
                    productToShopPage.setProductName(product.getProductName());
                    productToShopPage.setBrand(product.getBrand());
                    productToShopPage.setProductImage(productImage.getImage());
                    productList.add(productToShopPage);
                    break;
                }
            }
        }

        model.addAttribute("productList", productList);
        return "user/shop";
    }
    @GetMapping("/product-details")
    public String productDetails(@RequestParam("productId") Long productId,Model model){
        Product product=productService.getProductById(productId);
        List<String>colors=new ArrayList<>();
        List<String>img=new ArrayList<>();
        List<Long>sizes=new ArrayList<>();
        for(Variant variant:product.getVariants()){
            if(!colors.contains(variant.getColor())){
                colors.add(variant.getColor());
            }if(!sizes.contains(variant.getSize())){
                sizes.add(variant.getSize());
            }
        }
        List<ProductImage>images=productImageService.getProductImagesByProductColor(productId,colors.get(0));
        for(ProductImage image:images){
            img.add(image.getImage());
            System.out.println(image.getImage());
        }
        model.addAttribute("product",product);
        model.addAttribute("colors",colors);
        model.addAttribute("sizes",sizes);
        model.addAttribute("image",img);
        return "/user/product-details";
    }






}
