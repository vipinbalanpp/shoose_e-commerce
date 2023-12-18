package com.vipin.shoose.controller.auth;
import com.vipin.shoose.dto.ProductToShopPage;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.dto.VerifyOtpDto;
import com.vipin.shoose.exception.MailAlreadyExistException;
import com.vipin.shoose.exception.OtpInvalidException;
import com.vipin.shoose.model.Category;
import com.vipin.shoose.model.Product;
import com.vipin.shoose.model.ProductImage;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.CategoryRepository;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.service.*;
import com.vipin.shoose.util.OtpService;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.*;


@Controller
public class AuthController {
    @Autowired
    OtpService otpService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired

    CategoryRepository categoryRepository;
    @Autowired
    ProductImageService productImageService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("categories",categoryService.getAllActiveCategory());
        model.addAttribute("productList", productService.getProductToHome());
        return "user/home";

    }
    @GetMapping("/home")
    public String AuthRedirect(){
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            if(roles.contains("ADMIN")){
                return "redirect:/admin/home";
            }
            if(roles.contains("USER")){
                return "redirect:/user/home";
            }
        }
        return "redirect:/user/home";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam(name = "error", required=false)String error ,Model model){
        if(error != null){
            model.addAttribute("error","invalid credentials");
        }
        return "home";
    }
    @GetMapping("/login")
    public String login(){
        return "user/sign-in";
    }
    @GetMapping("/register")
    public String signUp(Model model){
        UserDto userDto=new UserDto();
        model.addAttribute("userDto",userDto);
        return "user/register";
    }
    @PostMapping("/register")
    public String registerPost(UserDto userDto,
                               HttpSession session,
                               RedirectAttributes redirectAttributes){
        User existingUserEmail=userRepository.findByEmail(userDto.getEmail());
        User existingUserByUsername=userRepository.findByUsername(userDto.getUsername());

        if(existingUserEmail!=null){
          String errorMessage="MailId you have entered  already exists";
          redirectAttributes.addFlashAttribute("mailError",errorMessage);
          return "redirect:/register";
        }else if(existingUserByUsername!=null){
            String errorMessage="Username you have entered  already exists";
            redirectAttributes.addFlashAttribute("mailError",errorMessage);
            return "redirect:/register";
        }
        String otp=otpService.generateOtp();
        userDto.setOtp(otp);
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),otp);
        session.setAttribute("unverifiedUser",userDto);
        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String verifyOtp(Model model,HttpSession session){
        UserDto userDto= (UserDto) session.getAttribute("unverifiedUser");
        model.addAttribute("userOtp",new VerifyOtpDto());
        return "user/signup-otp";
    }
    @PostMapping("/verify-otp")
    public String verifySuccess(@RequestParam("otp") String otp, HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("unverifiedUser");
        try{
           if( userService.verifyOtp(userDto, otp) ){
               session.removeAttribute("unverifiedUser");
               return "redirect:/login";
           }
        }catch (OtpInvalidException e){
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
        }

            return "user/signup-otp";

    }
    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session,RedirectAttributes redirectAttributes){
       UserDto userDto= (UserDto) session.getAttribute("unverifiedUser");
       String email=userDto.getEmail();
       String otp=otpService.generateOtp();
        System.out.println(otp);
       otpService.resendOTPEmail(email,otp);
       userDto.setOtp(otp);
       redirectAttributes.addFlashAttribute("resend","New otp has been sen to your email");
       return "redirect:/verify-otp";
    }
  @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "user/forgot-password";
  }
  @PostMapping("/forgot-password")
    public String forgotPasswordEmail(@RequestParam ("email") String email,
                                      HttpSession httpSession,
                                      RedirectAttributes redirectAttributes){
      System.out.println(email);
      if(!userRepository.existsByEmail(email)) {
          redirectAttributes.addFlashAttribute("notExists", "user does not exists. enter valid email");
          return "redirect:/forgot-password";
      }
      UserDto userDto=new UserDto();
      userDto.setEmail(email);
      userService.sendOtp(userDto);
     httpSession.setAttribute("userDto",userDto);
      return "redirect:/forgot-password-otp";
  }
  @GetMapping("/forgot-password-otp")
    public String forgotPasswordOtp(HttpSession httpSession,
                                    Model model){
        UserDto userDto= (UserDto) httpSession.getAttribute("userDto");
        return "user/forgot-otp-validation";
  }
    @PostMapping("/forgot-password-otp")
    public String forgotOtpVerification(@RequestParam ("otp") String otp,
                                        HttpSession httpSession,
                                        RedirectAttributes redirectAttributes){
        UserDto userDto= (UserDto) httpSession.getAttribute("userDto");
        if(!otpService.isOtpValid(userDto,otp)){
            redirectAttributes.addFlashAttribute("invalidOtp","Invalid otp. Enter the valid otp");
            return "redirect:/forgot-password-otp";
        }
        String email=userDto.getEmail();
        return "redirect:/new-password";
    }
    @GetMapping("/new-password")
    public String newPassword(HttpSession httpSession){
        UserDto userDto= (UserDto) httpSession.getAttribute("userDto");
        return "user/new-password-for-forgot-password";
    }
    @PostMapping("/new-password")
    public String newPasswordSetting(HttpSession httpSession,
                                     @RequestParam ("password") String password,
                                     RedirectAttributes redirectAttributes){
        UserDto userDto= (UserDto) httpSession.getAttribute("userDto");
        userDto.setPassword(password);
        userService.setNewPassword(userDto);
        httpSession.removeAttribute("userDto");
        redirectAttributes.addFlashAttribute("success","Your Password Changed Successfully.Now you can login with your new password");
        return "redirect:/login";
    }
    @PostMapping("/forgot-resend-otp")
    public String forgotResendOtp(HttpSession session,
                                  RedirectAttributes redirectAttributes){
        UserDto userDto= (UserDto) session.getAttribute("userDto");
        userService.resendMail(userDto);
        redirectAttributes.addFlashAttribute("resend","New Otp send to your email");
        return "redirect:/forgot-password-otp";
    }
    @GetMapping("/checkReferralId")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<Boolean> checkReferralId(@RequestParam("referralId")String referralId){
        return new ResponseEntity<>(  userService.verifyReferralId(referralId), HttpStatus.OK);
    }
    @GetMapping("/toShopByCategory")
    public String showProductsByCategory(@RequestParam Long categoryId, Model model) {
        System.out.println(categoryId);
        List<Product> products = productService.getProductsByCategory(categoryId);
        System.out.println(products);
        model.addAttribute("productList", products);
        return "user/shop";
    }

}
