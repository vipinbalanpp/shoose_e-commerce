package com.vipin.shoose.service;

import com.vipin.shoose.model.OrderInfo;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.Variant;
import com.vipin.shoose.repository.OrderRepository;
import com.vipin.shoose.repository.ProductRepository;
import com.vipin.shoose.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VariantServiceImpl implements  VariantService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    VariantRepository variantRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public List<Variant> getVaraints(Long id) {
        return variantRepository.findByProductId(id);
    }

    @Override
    public List<String> getColorsByProductId(Long productId) {

        return variantRepository.findColorsByProductId(productId);
    }

    @Override
    public void changeQuantity(Long variantId, Long quantity,Long productId) {

            Variant variant=variantRepository.findByVariantId(variantId);
        System.out.println(variant.getQuantity());
            variant.setQuantity(variant.getQuantity()+quantity);
            variantRepository.save(variant);
    }

    @Override
    public void addNewVarinats(Long productId,
                               List<String> variantColors,
                               List<Long> variantSizes,
                               List<Integer> variantQuantities) {

        List<Variant> variantsToAdd=new ArrayList<>();
        Product product=productRepository.findByProductId(productId);
        boolean variantExists=false;
        for(int i=0;i<variantColors.size();i++){
            variantExists=false;
            for(Variant variantExist:product.getVariants()){
                if (variantExist.getColor().equals(variantColors.get(i)) && variantExist.getSize().equals(variantSizes.get(i))) {
                    variantExist.setQuantity(variantExist.getQuantity()+variantQuantities.get(i));
                    product.setQuantity(product.getQuantity()+variantQuantities.get(i));
                    productRepository.save(product);
                    variantRepository.save(variantExist);
                    variantExists=true;
                    break;
                }
            }if(!variantExists){
                Variant variant=new Variant();
                variant.setColor(variantColors.get(i));
                variant.setSize(variantSizes.get(i));
                variant.setQuantity(Long.valueOf(variantQuantities.get(i)));
                variant.setProduct(product);
                product.setQuantity((variant.getQuantity()+product.getQuantity()));
                productRepository.save(product);
                variantsToAdd.add(variant);
            }
        }product.getVariants().addAll(variantsToAdd);
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);
        variantRepository.saveAll(variantsToAdd);
    }

    @Override
    public List<Long> getAllSizesForColor(Long productId, String selectedColor) {
        return variantRepository.findAllSizesByColor(productId,selectedColor);
    }

    @Override
    public Variant getVariantByProductColorAndSize(Long productId, String color, Long size) {
        return  variantRepository.findByProductIdColorAndSize(productId,color,size);
    }

    @Override
    public Long getVariantQuantity(Long productId, String color, Long size) {
        return getVariantByProductColorAndSize(productId,color,size).getQuantity();
    }

    @Override
    public void manageStockForOrder(Map<Variant, Long> orderProducts) {
        for(Variant variant:orderProducts.keySet()){
            Variant variant1=variantRepository.findByVariantId(variant.getVariantId());
            variant1.setQuantity(variant1.getQuantity()-orderProducts.get(variant));
            variantRepository.save(variant1);
        }
    }
    @Override
    public void updateStockonCancellingOrder(Long orderId) {
        OrderInfo orderInfo=orderRepository .findByOrderId(orderId);
        Map<Variant,Long>products=orderInfo.getProducts();
        for(Variant variant:products.keySet()){
            variant.setQuantity(variant.getQuantity()+products.get(variant));
        }
    }
}
