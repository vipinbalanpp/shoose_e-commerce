package com.vipin.shoose.controller.user;

import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.service.CartService;
import com.vipin.shoose.service.UserService;
import com.vipin.shoose.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    VariantService variantService;
    @Autowired
    UserService userService;
    @Autowired
    CartService cartService;
    @GetMapping("/user/cart")
    public String userCart(Model model){
        return "/user/cart";
    }
    @GetMapping("/getCartItems")
    public ResponseEntity<List<SelectedProducts>>cartItemsToCart(){
        List<SelectedProducts>productsInCart=cartService.mapProductDetails();
        if(productsInCart==null){
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        return new ResponseEntity<>(productsInCart,HttpStatus.OK);
    }
    @GetMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Boolean>addToCart(@RequestParam("productId") Long productId,
                                           @RequestParam("color")String color,
                                           @RequestParam("size")Long size){
        System.out.println("fdlkfjlsdfjs");
        if(cartService.addToCart(productId,color,size)){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
    @GetMapping("/getProductQuantity")
    @ResponseBody
    public ResponseEntity<Long>getProductQuantity(@RequestParam("productId") Long productId,
                                            @RequestParam("color")String color,
                                            @RequestParam("size")Long size){

      Long quantity=variantService.getVariantQuantity(productId,color,size);
        System.out.println(quantity);
      return new ResponseEntity<>(quantity,HttpStatus.OK);
    }

    @GetMapping("/removeItemFromCart")
    @ResponseBody
    public ResponseEntity<String>  removeItemFromCart(@RequestParam("variantId")Long variantId){
        cartService.removeItemFromCart(variantId);
        return new ResponseEntity<>("Item removed successfully",HttpStatus.OK);
    }
    @GetMapping("/increaseQuantity")
    @ResponseBody
    public ResponseEntity<String> increaseQuantity(@RequestParam("variantId")Long variantId){
        cartService.increaseQuantity(variantId);
        return new ResponseEntity<>("quantity increased",HttpStatus.OK);
    }
    @GetMapping("/decreaseQuantity")
    @ResponseBody
    public ResponseEntity<String> decreaseQuantity(@RequestParam("variantId")Long variantId){
        cartService.decreaseQuantity(variantId);
        return new ResponseEntity<>("quantity increased",HttpStatus.OK);
    }
}
