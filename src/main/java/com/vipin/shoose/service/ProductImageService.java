package com.vipin.shoose.service;

import com.vipin.shoose.model.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage>productImageList();

    List<ProductImage> getProductImagesByProductId(Long productId);


    List<ProductImage> getProductImagesByProductColor(Long productId, String s);

    List<String> getImagesFromProductImageList(List<ProductImage> images);


}
