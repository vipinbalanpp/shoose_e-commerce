package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.*;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.ProductImageRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.service.CategoryService;
import com.vipin.shoose.service.ProductImageService;
import com.vipin.shoose.service.ProductService;
import com.vipin.shoose.service.VariantService;
import com.vipin.shoose.util.ImageUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@Slf4j
public class ProductController {
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    VariantService variantService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/add-product")
    public String addProduct(Model model){
        ProductDto productDto=new ProductDto();
        List<Category> categories = categoryRepository.findAll();
        productDto.setVariants(new ArrayList<>());
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        return "admin/add-product";
    }
    @PostMapping("/add-product")
    public String addProductPost(@ModelAttribute ProductDto productDto,
                                 @RequestParam("variants.color") List<String> variantColors,
                                 @RequestParam("variants.size") List<Long> variantSizes,
                                 @RequestParam("variants.quantity") List<Long> variantQuantities,
                                 RedirectAttributes redirectAttributes ) throws IOException {
        Product existingProduct=productRepository.findByProductName(productDto.getProductName());
        if(existingProduct!=null){
            redirectAttributes.addFlashAttribute("productExists","Product exists");
            return "redirect:/admin/add-product";
        }

        Long productId= productService.saveProduct(productDto,variantColors,variantSizes,variantQuantities);
        redirectAttributes.addFlashAttribute(productId);
        return "redirect:/admin/add-product-image?productId=" + productId;
    }
    @GetMapping("/edit-product/{productId}")
    public String editProduct(@PathVariable("productId") Long productId, Model model){
        model.addAttribute("product",productService.getProductDetailsToEdit(productId));
        model.addAttribute("categories", categoryService.getCategoriesForEditingProduct(productId));
        return "admin/edit-product";
    }
    @PostMapping("/change-product-details/{productId}")
    public String changeProductDetails(@PathVariable("productId") Long productId,
                                       EditProductDto editProductDto,
                                       RedirectAttributes redirectAttributes){
        if(editProductDto==null){
            return "redirect:/admin/product-details/"+productId;
        }
        Product product=productRepository.findByProductName(editProductDto.getProductName());
        Product product1=productRepository.findByProductId(productId);
        if(product1 != product && product != null){
            redirectAttributes.addFlashAttribute("productExists","Product exists. Change the name");
            return "redirect:/admin/edit-product/"+productId;
        }
        productService.changeProductDetails(productId,editProductDto);
        return "redirect:/admin/product-details/"+productId;
    }

    @GetMapping("/add-product-image")
    public String addProductImage(@RequestParam("productId") Long productId, Model model){
        ProductImagesDTO productImagesDTO=new ProductImagesDTO();
        Product product= productRepository.findByProductId(productId);
        List<String> colors = productService.getProductColors(product);
        productImagesDTO.setProductId(productId);
        productImagesDTO.setColors(colors);
        model.addAttribute("product",productImagesDTO);
        return "admin/add-product-image";
    }
    @PostMapping("/add-product-images")
    public String saveProductImage(@ModelAttribute ProductImagesDTO productImagesDTO,
                                   RedirectAttributes redirectAttributes) throws IOException {
        Product product = productRepository.findByProductId(productImagesDTO.getProductId());
        for(MultipartFile image:productImagesDTO.getProductImages()){
            System.out.println(image);
        }
        productImagesDTO.setColors(product.getVariants().stream()
                .map(Variant::getColor)
                .distinct()
                .collect(Collectors.toList()));

        productImageService.saveProductImage(productImagesDTO,product);
        return "redirect:/admin/products-list";
    }


    @GetMapping("/product-details/{id}")
    public String getProductDetails(@PathVariable("id")Long productId, Model model){
        Product product=productService.getProductById(productId);
        List<Variant> variants = variantService.getVaraints(productId);
        model.addAttribute("product",product);
        model.addAttribute("variants",variants);
        return "admin/product-details";
    }
    @PostMapping("/increase-quantity/{productId},{variantId}")
    public String increaseQuantity( @RequestParam ("quantity") Long quantity,
                                    @PathVariable("productId")Long productId,
                                    @PathVariable("variantId") Long variantId){
        variantService.changeQuantity(variantId,quantity,productId);
        productService.changeQuantity(productId,quantity);
        return "redirect:/admin/product-details/" + productId;
    }
    @PostMapping("/block-product/{productId}")
    public String blockProduct(@PathVariable("productId") Long productId){
        Product product=productRepository.findByProductId(productId);
        product.setEnabled(false);
        productRepository.save(product);
        return "redirect:/admin/products-list";
    }
    @PostMapping("/unBlock-product/{productId}")
    public String unBlockProduct(@PathVariable("productId") Long productId){
        Product product=productRepository.findByProductId(productId);
        product.setEnabled(true);
        productRepository.save(product);
        return "redirect:/admin/products-list";
    }
    @PostMapping("/new-variants/{productId}")
    public String newVariants(  @RequestParam("variants.color") List<String> variantColors,
                                @RequestParam("variants.size") List<Long> variantSizes,
                                @RequestParam("variants.quantity") List<Integer> variantQuantities,
                                @PathVariable("productId") Long productId){Product product=productRepository.findByProductId(productId);

        variantService.addNewVarinats(productId,variantColors,variantSizes,variantQuantities);
        return "redirect:/admin/product-details/"+productId;
    }
    @GetMapping("/products-list")
    public String productList(Model model){
        List<Product> allProducts=productService.getAllProducts();
        UserDto userDto=new UserDto();
        model.addAttribute(userDto);
        model.addAttribute("products",allProducts);
        return "admin/product-list";
    }
    @GetMapping("/update-product-image/{productId}")
    public String updateProductImage(@PathVariable("productId")Long productId, Model model){
        List<ProductImage>productImages=productImageService.getProductImagesByProductId(productId);
        model.addAttribute("images",productImages);
        model.addAttribute("productId",productId);
        return "admin/edit-product-images";
    }
    @PostMapping("/update-product-image/{productId}")
    public String getUpdateProductPage(@PathVariable("productId")Long productId){
        return  "redirect:/admin/update-product-image/"+productId;
    }
    @PostMapping("/change-product-image")
    public String changeProductImage(@RequestParam("imageId") Long imageId,
                                                     @RequestParam("image") MultipartFile newImage,
                                     @RequestParam("productId")Long productId) throws IOException {
productImageService.changeProductImage(imageId,newImage);
        return "redirect:/admin/update-product-image/"+productId;
    }


}
