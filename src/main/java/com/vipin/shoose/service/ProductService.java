package com.vipin.shoose.service;

import com.vipin.shoose.dto.EditProductDto;
import com.vipin.shoose.dto.ProductDto;
import com.vipin.shoose.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
   public Long addProductAndGetId(ProductDto productDto);

   List<Product> getAllActiveProducts();

   Product getProductById(Long id);

    void changeQuantity(Long productId, Long quantity);

    void changeProductDetails(Long productId, EditProductDto editProductDto);

    List<Product> getAllProducts();
}
