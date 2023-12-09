package com.vipin.shoose.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.net.http.HttpRequest;

@Configuration
public class WebSecurityconfig {
    @Autowired
    SuccessHandler successHandler;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http,UserDetailsService userDetailsService)throws Exception{
        http.authorizeHttpRequests(request->
                        request.requestMatchers("/","/shop","/register","/verify-otp","/resend-otp","/forgot-password",
                                "/forgot-password-otp","/new-password","/forgot-resend-otp","/checkReferralId","/product-details").permitAll()
                                .requestMatchers("/user/**").hasAnyAuthority("USER","ADMIN").
                        requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated())
                .formLogin(formLogin->
                        formLogin.loginPage("/login").successHandler(successHandler).permitAll())
                .logout(logout->
                        logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .rememberMe(rememberMe->
                        rememberMe.rememberMeServices(rememberMeServices(userDetailsService))
                                .userDetailsService(userDetailsService).key("Token")
                );
        return http.build();
    }
    @Bean
    public RememberMeServices rememberMeServices(UserDetailsService userDetailsService){
        TokenBasedRememberMeServices tokenBasedRememberMeServices=
                new TokenBasedRememberMeServices("Token",userDetailsService);
        tokenBasedRememberMeServices.setAlwaysRemember(true);
        return tokenBasedRememberMeServices;
    }
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/img/**","/css/**");
    }

}
