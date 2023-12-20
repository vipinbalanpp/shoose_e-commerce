package com.vipin.shoose.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/product-images/**")
                .addResourceLocations("/home/ubuntu/prod/src/main/resources/static/img/product-images/");
    }
}
