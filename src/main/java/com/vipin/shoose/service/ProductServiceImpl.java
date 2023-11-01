package com.vipin.shoose.service;

import com.vipin.shoose.dto.EditProductDto;
import com.vipin.shoose.dto.ProductDto;
import com.vipin.shoose.dto.VariantDto;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    VariantRepository variantRepository;
    @Override
    public Long addProductAndGetId(ProductDto productDto) {

        Product product=new Product();
       Category category=categoryRepository.findByCategoryId(productDto.getCategoryId());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(Double.valueOf(productDto.getPrice()));
        product.setBrand(productDto.getBrand());
        product.setGender(productDto.getGender());
        product.setCategory(category);
        product.setLastUpdated (LocalDateTime.now());
        productRepository.save(product);
        for(int i=0;i<productDto.getVariants().size();i++){
            Variant variant=new Variant();
            variant.setProduct(product);
            variant.setColor(productDto.getVariants().get(i).getColor());
            variant.setSize(Long.valueOf(productDto.getVariants().get(i).getSize()));
            variant.setQuantity(Long.valueOf(productDto.getVariants().get(i).getQuantity()));
            variantRepository.save(variant);
        }
        Long quantity= 0L;
        for(VariantDto variant:productDto.getVariants()){
            quantity= quantity+ Long.valueOf(variant.getQuantity());
        }
        product.setQuantity(Math.toIntExact(quantity));
        productRepository.save(product);
return product.getProductId();

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
        product.setQuantity((int) (product.getQuantity()+quantity));
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
        if(editProductDto.getPrice()!=""){
            product.setPrice(Double.valueOf(editProductDto.getPrice()));
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
}
