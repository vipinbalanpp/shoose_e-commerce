package com.vipin.shoose.controller.admin;

import com.vipin.shoose.dto.*;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.ProductImageRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.service.ProductService;
import com.vipin.shoose.service.VariantService;
import com.vipin.shoose.util.ImageUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    VariantService variantService;
    @Autowired
    ImageUpload imageUpload;

    @GetMapping("admin/add-product")
    public String addProduct(Model model){
        ProductDto productDto=new ProductDto();
        List<Category> categories = categoryRepository.findAll();
        productDto.setVariants(new ArrayList<>());
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);
        return "admin/add-product";
    }
    @PostMapping("admin/add-product")
    public String addProductPost(@ModelAttribute ProductDto productDto,
                                 @RequestParam("variants.color") List<String> variantColors,
                                 @RequestParam("variants.size") List<Long> variantSizes,
                                 @RequestParam("variants.quantity") List<Integer> variantQuantities,
                                 RedirectAttributes redirectAttributes ) {
        Product existingProduct=productRepository.findByProductName(productDto.getProductName());
        if(existingProduct!=null){
            redirectAttributes.addFlashAttribute("productExists","Product exists");
            return "redirect:/admin/add-product";
        }
        List<VariantDto> variants = new ArrayList<>();
        for (int i = 0; i < variantColors.size(); i++) {
            VariantDto variantDto = new VariantDto();
            variantDto.setColor(variantColors.get(i));
            variantDto.setSize(String.valueOf(variantSizes.get(i)));
            variantDto.setQuantity(String.valueOf(variantQuantities.get(i)));
            variants.add(variantDto);
        }
        productDto.setVariants(variants);
        Long productId= productService.addProductAndGetId(productDto);
        redirectAttributes.addFlashAttribute(productId);
        return "redirect:/admin/add-product-image?productId=" + productId;

    }
    @GetMapping("/admin/edit-product/{productId}")
    public String editProduct(@PathVariable("productId") Long productId, Model model){
        EditProductDto productDto=new EditProductDto();
        Product product=productRepository.findByProductId(productId);
        List<Category>categories=categoryRepository.findAll();
        categories.remove(product.getCategory());
        model.addAttribute("productDto",productDto);
        model.addAttribute("categories",categories);
        return "/admin/edit-product";
    }
    @PostMapping("/admin/change-product-details/{productId}")
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

    @GetMapping("admin/add-product-image")
    public String addProductImage(@RequestParam("productId") Long productId, Model model){
        ProductImagesDTO productImagesDTO=new ProductImagesDTO();
        Product product= productRepository.findById(productId).get();
        log.info(" "+product.getVariants().isEmpty());
        log.info("IS nULL" + Objects.isNull(product.getVariants()));
        log.info("prduct IS nULL" + Objects.isNull(product));
        List<String> colors = product.getVariants().stream()
                .map(Variant::getColor)
                .distinct()
                .collect(Collectors.toList());
        productImagesDTO.setProductId(productId);
        productImagesDTO.setColors(colors);
        model.addAttribute("product",productImagesDTO);
        return "/admin/add-product-image";
    }

    @PostMapping("/admin/add-product-images")
    public String saveProductImage(@ModelAttribute ProductImagesDTO productImagesDTO,
                                   RedirectAttributes redirectAttributes) throws IOException {
        Product product = productRepository.findById(productImagesDTO.getProductId()).get();
        log.info(" "+product.getVariants().isEmpty());
        log.info("IS nULL" + Objects.isNull(product.getVariants()));
        log.info("prduct IS nULL" + Objects.isNull(product));
        productImagesDTO.setColors(product.getVariants().stream()
                .map(Variant::getColor)
                .distinct()
                .collect(Collectors.toList()));

        System.out.println(productImagesDTO.getProductImages());
        for(int i=0;i<productImagesDTO.getColors().size();i++){
            for(int j=0;j<3;j++){
                ProductImage productImage=new ProductImage();
                productImage.setImage(imageUpload.saveImage(productImagesDTO.getProductImages().get(i+j)));
                productImage.setProduct(product);
                productImage.setColor(productImagesDTO.getColors().get(i));
                productImageRepository.save(productImage);
            }
        }
        product.setEnabled(true);
        productRepository.save(product);

        return "redirect:/admin/products-list";
    }


    @GetMapping("admin/product-details/{id}")
    public String getProductDetails(@PathVariable("id")Long productId, Model model){
        Product product=productService.getProductById(productId);
        List<Variant> variants = variantService.getVaraints(productId);
        model.addAttribute("product",product);
        model.addAttribute("variants",variants);
        return "/admin/product-details";
    }
    @PostMapping("/admin/increase-quantity/{productId},{variantId}")
    public String increaseQuantity( @RequestParam ("quantity") Long quantity,
                                    @PathVariable("productId")Long productId,
                                    @PathVariable("variantId") Long variantId){
        System.out.println(quantity);
        System.out.println(productId);
        System.out.println(variantId);
        variantService.changeQuantity(variantId,quantity,productId);
        productService.changeQuantity(productId,quantity);
        return "redirect:/admin/product-details/" + productId;
    }
    @PostMapping("/admin/block-product/{productId}")
    public String blockProduct(@PathVariable("productId") Long productId){
        Product product=productRepository.findByProductId(productId);
        product.setEnabled(false);
        productRepository.save(product);
        return "redirect:/admin/products-list";
    }
    @PostMapping("/admin/unBlock-product/{productId}")
    public String unBlockProduct(@PathVariable("productId") Long productId){
        Product product=productRepository.findByProductId(productId);
        product.setEnabled(true);
        productRepository.save(product);
        return "redirect:/admin/products-list";
    }
    @PostMapping("/admin/new-variants/{productId}")
    public String newVariants(  @RequestParam("variants.color") List<String> variantColors,
                                @RequestParam("variants.size") List<Long> variantSizes,
                                @RequestParam("variants.quantity") List<Integer> variantQuantities,
                                @PathVariable("productId") Long productId){Product product=productRepository.findByProductId(productId);

        variantService.addNewVarinats(productId,variantColors,variantSizes,variantQuantities);
        return "redirect:/admin/product-details/"+productId;
    }
    @GetMapping("admin/products-list")
    public String productList(Model model){
        List<Product> allProducts=productService.getAllProducts();
        UserDto userDto=new UserDto();
        model.addAttribute(userDto);
        model.addAttribute("products",allProducts);
        return "admin/product-list";
    }
}
