package com.vipin.shoose.controller;
import com.vipin.shoose.dto.*;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.ColorRepository;
import com.vipin.shoose.repository.SizeRepository;
import com.vipin.shoose.repository.VariantRepository;
import com.vipin.shoose.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    VariantService variantService;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ColorService colorService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ColorRepository colorRepository;

    @GetMapping("/admin/home")
    public String home(){
        return "admin/home";
    }


    @GetMapping("admin/add-product")
    public String addProduct(Model model){
        ProductDto productDto=new ProductDto();
        List<Category> categories = categoryRepository.findAll();
        List<Color> allColors = colorRepository.findAll();
        List<Size>allSizes=sizeRepository.findAll();
        productDto.setVariants(new ArrayList<>());
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        model.addAttribute("allColors", allColors);
        model.addAttribute("allSizes",allSizes);
        return "admin/add-product";
    }
    @GetMapping("admin/add-product-image")
    public String addProductImage(@RequestParam("productId") Long productId, Model model){
     List<String>colors=variantService.getColorsByProductId(productId);
     model.addAttribute("colors",colors);
        return "/admin/add-product-image";
    }
    @GetMapping("admin/product-details/{id}")
    public String getProductDetails(@PathVariable("id")Long id, Model model){
        Product product=productService.getProductById(id);
        List<Variant> variants = variantService.getVaraints(id);
        model.addAttribute("variants",variants);
        model.addAttribute("product",product);
        return "/admin/product-details";
    }
    @GetMapping("admin/customers-list")
    public String customerList(Model model){
        List<User> allUsers=userService.getAllUsers();
        List<User>users=new ArrayList<>();
        for(User user:allUsers){
            if ("USER".equals(user.getRoles())) {
                users.add(user);
            }
        }
        model.addAttribute("users",users);
        return "admin/customers-list";
    } @PostMapping("admin/block-user/{id}")
    public String blockUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        userService.blockUser(id);
        String blocked="user blocked";
        redirectAttributes.addFlashAttribute("blocked",blocked);
        return "redirect:/admin/customers-list";
    }

    @PostMapping("admin/unblock-user/{id}")
    public String unblockUser(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        userService.unBlockUser(id);
        String unBlocked="user unblocked";
        redirectAttributes.addFlashAttribute("unblocked",unBlocked);
        return "redirect:/admin/customers-list";
    }

    @PostMapping("admin/add-product")
    public String addProductPost(@ModelAttribute ProductDto productDto,
                                 @RequestParam("variants.color") List<String> variantColors,
                                 @RequestParam("variants.size") List<String> variantSizes,
                                 @RequestParam("variants.quantity") List<Integer> variantQuantities,
                                  RedirectAttributes redirectAttributes ) {

        List<VariantDto> variants = new ArrayList<>();
        for (int i = 0; i < variantColors.size(); i++) {
            VariantDto variantDto = new VariantDto();
            variantDto.setColor(variantColors.get(i));
            variantDto.setSize(variantSizes.get(i));
            variantDto.setQuantity(String.valueOf(variantQuantities.get(i)));
            variants.add(variantDto);
        }
        productDto.setVariants(variants);
       Long productId= productService.addProductAndGetId(productDto);
        redirectAttributes.addFlashAttribute(productId);
        return "redirect:/admin/add-product-image?productId=" + productId;

    }

    @PostMapping("admin/add-category")
    public String addCategory(CategoryDto categoryDto,RedirectAttributes redirectAttributes){
        Category existingCategory=categoryService.getCategoryByCategoryName(categoryDto.getCategoryName());
        if(existingCategory!=null){
            redirectAttributes.addFlashAttribute("categoryExists","Category exists");
            return "redirect:/admin/categories";
        }
            categoryService.addCategory(categoryDto);
        return "redirect:/admin/categories";
    }

    @GetMapping("admin/products-list")
    public String productList(Model model){
        List<Product> allProducts=productService.getAllProducts();
        UserDto userDto=new UserDto();
        ColorDto colorDto=new ColorDto();
        model.addAttribute(userDto);
        model.addAttribute("color",colorDto);
        model.addAttribute("products",allProducts);
        return "admin/product-list";
    }

    @GetMapping("admin/orders-list")
    public String ordersList(){
        return "admin/orders-list";
    }
    @GetMapping("admin/coupons")
    public String coupons(){
        return "admin/coupon";
    }
    @GetMapping("admin/banners")
    public String banners(){
        return "admin/banners";
    }
    @GetMapping("/admin/categories")
    public String categories(Model model){
        List<Category> allCategories=categoryService.getAllCategory();
        model.addAttribute("categories",allCategories);
        return "admin/categories";
    }
    @PostMapping("/admin/rename-category")
    public String renameCategory(CategoryDto categoryDto,RedirectAttributes redirectAttributes){
        categoryService.changeCategoryName(categoryDto);
        return "redirect:/categories";

    }


}
