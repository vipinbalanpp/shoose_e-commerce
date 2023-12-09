package com.vipin.shoose.service;

import com.vipin.shoose.dto.EditProductDto;
import com.vipin.shoose.dto.ProductDto;
import com.vipin.shoose.dto.ProductToShopPage;
import com.vipin.shoose.dto.VariantDto;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.OrderRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.repository.VariantRepository;
import com.vipin.shoose.util.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageUpload imageUpload;

    @Autowired
    VariantRepository variantRepository;
    @Override
    public Long saveProduct(ProductDto productDto,
                            List<String>variantColors,
                            List<Long>variantSizes,
                            List<Long>variantQuantities) throws IOException {
        removeDuplicates(variantColors,variantSizes,variantQuantities);
        List<Variant>variants= createVariants(variantColors,variantSizes,variantQuantities);
       Product product=createProductAndMapFields(productDto,variants);
       for(Variant variant:variants){
           variant.setProduct(product);
           variantRepository.save(variant);
       }product.setImage(imageUpload.saveImage(productDto.getImage()));
       if(categoryRepository.findByCategoryId(productDto.getCategoryId()).getIsHavingOffer()){
           product.setDiscountPrice(productDto.getPrice()-productDto.getPrice()*categoryRepository.findByCategoryId(productDto.getCategoryId()).getOfferInPercentage()/100);
           product.setIsCategoryHavingOffer(true);
       }
        productRepository.save(product);
        return product.getProductId();
    }
    private Product createProductAndMapFields(ProductDto productDto,
                                              List<Variant> variants) {
        Product product=new Product();
        Category category=categoryRepository.findByCategoryId(productDto.getCategoryId());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setActualPrice(productDto.getPrice());
        product.setBrand(productDto.getBrand());
        product.setGender(productDto.getGender());
        product.setCategory(category);
        product.setLastUpdated (LocalDateTime.now());
        product.setVariants(variants);
        Long quantity= 0L;
        for(Variant variant:variants){
            quantity= quantity+variant.getQuantity();
        }
        product.setQuantity(quantity);
        System.out.println("varinats :" + variants);
        productRepository.save(product);
        return product;
    }
    private List<Variant> createVariants(List<String> variantColors,
                                         List<Long> variantSizes,
                                         List<Long> variantQuantities) {
        List<Variant> variants = new ArrayList<>();
        for (int i = 0; i < variantColors.size(); i++) {
            Variant variant = new Variant();
            variant.setColor(variantColors.get(i));
            variant.setSize(variantSizes.get(i));
            variant.setQuantity(variantQuantities.get(i));
            variantRepository.save(variant);
            variants.add(variant);
        }return variants;
    }
    private void removeDuplicates(List<String>variantColors,List<Long>variantSizes,List<Long>variantQuantities) {
        System.out.println(variantColors);
        System.out.println(variantSizes);
        System.out.println(variantQuantities);
        for(int i=0;i<variantColors.size()-1;i++){
            for(int j=i+1;j<variantColors.size();j++){
                if(Objects.equals(variantColors.get(i), variantColors.get(j)) && Objects.equals(variantSizes.get(i), variantSizes.get(j))){
                    variantQuantities.set(i,variantQuantities.get(i)+variantQuantities.get(j));
                    variantColors.remove(variantColors.get(j));
                    variantSizes.remove(variantSizes.get(j));
                    variantQuantities.remove(variantQuantities.get(j));
                }
            }
        }
        System.out.println(variantColors);
        System.out.println(variantSizes);
        System.out.println(variantQuantities);
    }
    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByEnabled();
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findByProductId(id);
    }

    @Override
    public void changeQuantity(Long productId, Long quantity) {
        Product product=productRepository.findByProductId(productId);
        product.setQuantity( (product.getQuantity()+quantity));
        productRepository.save(product);
    }
    @Override
    public void changeProductDetails(Long productId, EditProductDto editProductDto) {
        Product product=productRepository.findByProductId(productId);
        if(editProductDto.getProductName()!=""){
            product.setProductName(editProductDto.getProductName());

        }
        if(editProductDto.getDescription()!=""){
            product.setDescription(editProductDto.getDescription());
        }
        if(editProductDto.getPrice()!=null){
            product.setActualPrice(editProductDto.getPrice());
        }
        if(editProductDto.getBrand()!=""){
            product.setBrand(editProductDto.getBrand());
        }
        if(editProductDto.getGender()!=""){
            product.setGender(editProductDto.getGender());
        }
        if(editProductDto.getCategoryId()!=null){
         Category category=categoryRepository.findByCategoryId(editProductDto.getCategoryId());
            product.setCategory(category);

        }
        productRepository.save(product);
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<String> getProductColors(Product product) {

        return   product.getVariants().stream()
                .map(Variant::getColor)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getProductSizesForColor(Long productId, String selectedColor) {
        List<Long>sizes=new ArrayList<>();
        Product product=productRepository.findByProductId(productId);
        for(Variant variant:product.getVariants()){
            if(Objects.equals(variant.getColor(), selectedColor)){
                sizes.add(variant.getSize());
            }
        }return sizes;
    }

    @Override
    public Long getQuantityofColorVariant(Long productId, String selectedColor,Long selectedSize) {
      Variant variant=variantRepository.findByProductIdColorAndSize(productId,selectedColor,selectedSize);
      return variant.getQuantity();
    }

    @Override
    public ProductDto getProductDetailsToEdit(Long productId) {
        Product product=productRepository.findByProductId(productId);
        ProductDto productDto=new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getActualPrice());
        productDto.setBrand(product.getBrand());
        return productDto;
    }

    @Override
    public void setOfferToProducts(Long categoryId) {
        Category category=categoryRepository.findByCategoryId(categoryId);
        List<Product>products=productRepository.findByCategory(category);
        products.forEach(product -> {
            product.setDiscountPrice(product.getActualPrice()-(product.getActualPrice()*category.getOfferInPercentage()/100));
            product.setIsCategoryHavingOffer(true);
            productRepository.save(product);
        });
    }

    @Override
    public  List<Product> getProductToHome() {
        return getAllActiveProducts().stream()
                .filter(product -> categoryRepository.findByCategoryId(product.getCategory().getCategoryId()).isEnabled())
                .toList();

    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return getAllActiveProducts().stream()
                .filter(product -> categoryRepository.findByCategoryId(product.getCategory().getCategoryId()).isEnabled())
                .toList();
    }
}
