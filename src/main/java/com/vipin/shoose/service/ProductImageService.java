package com.vipin.shoose.service;

import com.vipin.shoose.dto.ProductImagesDTO;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    List<ProductImage>productImageList();

    List<ProductImage> getProductImagesByProductId(Long productId);


    List<ProductImage> getProductImagesByProductColor(Long productId, String s);

    List<String> getImagesFromProductImageList(List<ProductImage> images);


    void saveProductImage(ProductImagesDTO productImagesDTO, Product product);

    String getProductImage(Variant variant);

    void changeProductImage(Long imageId, MultipartFile newImage) throws IOException;
}
