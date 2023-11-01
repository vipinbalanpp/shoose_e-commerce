package com.vipin.shoose.controller.user;

import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/getProductImages")
    public ResponseEntity<Map<String, List<String>>> getProductImages(@RequestParam("selectedColor") String selectedColor,
                                                                      @RequestParam("productId") Long productId ){
        Map<String, List<String>> response = new HashMap<>();

        // Retrieve product images based on the selected color
        List<ProductImage> productImages = productImageService.getProductImagesByProductColor(productId,selectedColor);
        List<String> images=new ArrayList<>();
        for(ProductImage image:productImages){
            images.add(image.getImage());
        }
        System.out.println(images);

        if (images != null && !productImages.isEmpty()) {
            response.put("images", images);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Handle the case when no images are found for the selected color
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

