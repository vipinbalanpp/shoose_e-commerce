package com.vipin.shoose.service;

import com.vipin.shoose.dto.EditProductDto;
import com.vipin.shoose.dto.ProductDto;
import com.vipin.shoose.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface ProductService {
   public Long saveProduct(ProductDto productDto,List<String>variantColors,List<Long>variantSizes,List<Long>variantQuantities) throws IOException;

   List<Product> getAllActiveProducts();

   Product getProductById(Long id);

    void changeQuantity(Long productId, Long quantity);

    void changeProductDetails(Long productId, EditProductDto editProductDto);

    List<Product> getAllProducts();

    List<String> getProductColors(Product product);

    List<Long> getProductSizesForColor(Long productId, String selectedColor);

    Long getQuantityofColorVariant(Long productId, String selectedColor,Long selectedSize);


    ProductDto getProductDetailsToEdit(Long productId);

    void setOfferToProducts(Long categoryId);

    List<Product> getProductToHome();

    List<Product> getProductsByCategory(Long categoryId);
}
