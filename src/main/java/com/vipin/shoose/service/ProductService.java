package com.vipin.shoose.service;

import com.vipin.shoose.dto.ProductDto;
import com.vipin.shoose.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
   public Long addProductAndGetId(ProductDto productDto);

   List<Product> getAllProducts();

   Product getProductById(Long id);
}
