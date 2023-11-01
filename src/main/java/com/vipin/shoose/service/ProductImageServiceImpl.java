package com.vipin.shoose.service;

import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService{
    @Autowired
    ProductImageRepository productImageRepository;
    @Override
    public List<ProductImage> productImageList() {
        return productImageRepository.findAll();
    }

    @Override
    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public List<ProductImage> getProductImagesByProductColor(Long productId, String color) {
        return productImageRepository.findByProductColor(productId,color);
    }

    @Override
    public List<String> getImagesFromProductImageList(List<ProductImage> images) {
       List<String> img=new ArrayList<>();
       for(ProductImage image:images){
           img.add(image.getImage());
       }
       return img;
    }


}
