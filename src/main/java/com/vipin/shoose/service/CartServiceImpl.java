package com.vipin.shoose.service;

import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.exception.CustomException;
import com.vipin.shoose.model.*;
import com.vipin.shoose.repository.CartRepository;
import com.vipin.shoose.repository.ProductImageRepository;
import com.vipin.shoose.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    VariantService variantService;
    @Autowired
    ProductService productService;
    @Autowired
    VariantRepository variantRepository;
    @Autowired
     UserService userService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductImageRepository productImageRepository;

    @Override
    public boolean addToCart(Long productId, String color, Long size) {
        try {
            Variant variant= variantService.getVariantByProductColorAndSize(productId,color,size);
            System.out.println(variant.getProduct().getActualPrice());
            User user=userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            System.out.println("this method called");
                if(cart.getProducts().containsKey(variant)){
                    System.out.println("this also worked");
                    return false;
                }

            cart.setProducts(variant,1L);
            cartRepository.save(cart);
            return true;
        }catch (Exception e){
            throw new RuntimeException("An error Occurred");
        }

    }



    @Override
    public List<SelectedProducts> mapProductDetails() {
        try {
            User user=userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            Map<Variant,Long>variants=cart.getProducts();
            List<SelectedProducts>productToCartDtos=new ArrayList<>();
            for(Variant variant:variants.keySet()){
                SelectedProducts productToCart=new SelectedProducts();
                List<ProductImage>images=productImageRepository.findByProductColor(variant.getProduct().getProductId(),variant.getColor());
                productToCart.setImage(images.get(0).getImage());
                productToCart.setVariantId(variant.getVariantId());
                productToCart.setStock(variant.getQuantity());
                productToCart.setProductId(variant.getProduct().getProductId());
                productToCart.setProductName(variant.getProduct().getProductName());
                productToCart.setColor(variant.getColor());
                productToCart.setActualPrice(variant.getProduct().getActualPrice());
                productToCart.setDiscountPrice(variant.getProduct().getDiscountPrice());
                if(variant.getProduct().getIsCategoryHavingOffer()){
                    productToCart.setAmountToBePayed((int) (variant.getProduct().getDiscountPrice()*variants.get(variant)));
                }else {
                    productToCart.setAmountToBePayed((int) (variant.getProduct().getActualPrice()*variants.get(variant)));
                }
                productToCart.setActualTotalAmount((int) (variant.getProduct().getActualPrice()*variants.get(variant)));
                productToCart.setTotalDiscountAmount(productToCart.getActualTotalAmount()-productToCart.getAmountToBePayed());
                productToCart.setIsCategoryHavingOffer(variant.getProduct().getIsCategoryHavingOffer());
                productToCart.setSize(variant.getSize());
                productToCart.setQuantity(variants.get(variant));
                productToCartDtos.add(productToCart);
            }

            return productToCartDtos;
        }catch (Exception e){
            throw new CustomException("An error Occurred");
        }

    }

    @Override
    public void removeItemFromCart(Long variantId) {
        try {
            User user = userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            cart.getProducts().remove(variantRepository.findByVariantId(variantId));
            cartRepository.save(cart);
        }catch (Exception e){
            throw  new RuntimeException();
        }
    }

    @Override
    public List<SelectedProducts> getProductsToCheckOut() {
        try {
            List<SelectedProducts>products=new ArrayList<>();
            User user=userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            for (Variant variant:cart.getProducts().keySet()){
                SelectedProducts productToCartDto=new SelectedProducts();
                productToCartDto.setProductName(variant.getProduct().getProductName());
                List<ProductImage>images=productImageRepository.findByProductColor(variant.getProduct().getProductId(),variant.getColor());
                productToCartDto.setImage(images.get(0).getImage());
                productToCartDto.setColor(variant.getColor());
                productToCartDto.setSize(variant.getSize());
                productToCartDto.setIsCategoryHavingOffer(variant.getProduct().getIsCategoryHavingOffer());
                productToCartDto.setDiscountPrice(variant.getProduct().getDiscountPrice());
                productToCartDto.setActualPrice(variant.getProduct().getActualPrice());
                productToCartDto.setQuantity(cart.getProducts().get(variant));
                products.add(productToCartDto);
            }return products;
        }catch (Exception e){
            throw new RuntimeException("Error");
        }

    }

    @Override
    public void increaseQuantity(Long variantId) {
        try {
            User user=userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            cart.getProducts().put(variantRepository.findByVariantId(variantId),cart.getProducts().get(variantRepository.findByVariantId(variantId))+1);
            cartRepository.save(cart);
        }catch (Exception e){
            throw new RuntimeException("error");
        }


    }

    @Override
    public void decreaseQuantity(Long variantId) {
        try {
            User user=userService.getCurrentUser();
            Cart cart=cartRepository.findByUserId(user.getUserId());
            System.out.println(cart.getProducts().get(variantRepository.findByVariantId(variantId)));
            cart.getProducts().put(variantRepository.findByVariantId(variantId),cart.getProducts().get(variantRepository.findByVariantId(variantId))-1);
            cartRepository.save(cart);
        }catch (Exception e){
            throw new RuntimeException("error");
        }


    }

    @Override
    public Float getTotalAmount(List<SelectedProducts> products) {
        try {
            float totalAmount= 0.0F;
            for(SelectedProducts product:products){
                if(product.getIsCategoryHavingOffer()){
                    totalAmount+=product.getDiscountPrice();
                }else {
                    totalAmount+=product.getActualPrice();
                }
            }
            return totalAmount;
        }catch (Exception e){
            throw new RuntimeException("error");
        }

    }

    @Override
    public void clearCart() {
        try {
            Cart cart=cartRepository.findByUserId(userService.getCurrentUser().getUserId());
            cart.getProducts().clear();
        }catch (Exception e){
            throw new RuntimeException("error");
        }


    }

    @Override
    public Map<Variant, Long> getProducts() {
        try {
            return cartRepository.findByUserId(userService.getCurrentUser().getUserId()).getProducts();
        }catch (Exception e){
            throw new RuntimeException("error");
        }

    }

    @Override
    public void createCart(User user) {
        Cart cart=new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }


}
