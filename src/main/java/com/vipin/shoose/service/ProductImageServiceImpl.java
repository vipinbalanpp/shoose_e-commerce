package com.vipin.shoose.service;

import com.vipin.shoose.dto.ProductImagesDTO;
import com.vipin.shoose.exception.ProductImageNotFoundException;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.ProductImageRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.util.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService{
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    ImageUpload imageUpload;
    @Autowired
    ProductRepository productRepository;

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

    @Override
    public void saveProductImage(ProductImagesDTO productImagesDTO, Product product) {
        try {
            int k=0;
            for(int i=0;i<productImagesDTO.getColors().size();i++){
                for(int j=0;j<3;j++){
                    ProductImage productImage=new ProductImage();
                    productImage.setImage(imageUpload.saveImage(productImagesDTO.getProductImages().get(k)));
                    productImage.setProduct(product);
                    productImage.setColor(productImagesDTO.getColors().get(i));
                    productImageRepository.save(productImage);
                    k++;
                }
            }
            product.setEnabled(true);
            productRepository.save(product);

        }catch (Exception e){
            throw new RuntimeException("image Not found");
        }

    }

    @Override
    public String getProductImage(Variant variant) {
        if(productImageRepository.findByProductColor(variant.getProduct().getProductId(),variant.getColor()).get(0)!=null){
            return productImageRepository.findByProductColor(variant.getProduct().getProductId(),variant.getColor()).get(0).getImage();
        }else {
            throw new ProductImageNotFoundException("Product Image Not Exists");
        }

    }

    @Override
    public void changeProductImage(Long imageId, MultipartFile newImage) throws IOException {
        ProductImage image=productImageRepository.findByImageId(imageId);
        image.setImage(imageUpload.saveImage(newImage));
        productImageRepository.save(image);
    }


}
