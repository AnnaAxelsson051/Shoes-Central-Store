package com.shopme.admin.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfiguration {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    
    //Authenttication by looking up user in db
   @Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
     
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
 
    return authProvider;
}
   
   @Bean
   public AuthenticationManager authenticationManager(
           AuthenticationConfiguration authConfig) throws Exception {
       return authConfig.getAuthenticationManager();
   }
      
 
    //http.authenticationProvider(authenticationProvider());
    
   //Admin can access users module and settings
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
       //http.authorizeRequests()
	   http.authorizeHttpRequests()
	           .requestMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
       //Admin can access users, settings, countries, states and everything after that
               .requestMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
               .requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
                
               .requestMatchers("/products/new**", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
               
               .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                   .hasAnyAuthority("Admin", "Editor", "Salesperson")
               
               //Prod listing page, url for products and prod details, url for pagination
               .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
                   .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
               
                   //Everything bout products
               .requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
               .requestMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")               
               .requestMatchers("/customers/**", "/orders/**", "/get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")
               .requestMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
               .anyRequest().authenticated()
               .and()
               .formLogin()
               .loginPage("/login")
                   .usernameParameter("email")
                   .permitAll()
               .and().logout().permitAll()
               .and()
                    .rememberMe()
                     .key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                     .tokenValiditySeconds(14 * 24 * 60 * 60) //2 week  
               .and()
               .logout().permitAll();

       http.headers().frameOptions().sameOrigin();

       return http.build();
   }
  
   
 
    //Ignoring authentication for images js and webjars directory
    //So can be displayed when not logged in
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
 
}

/*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/users/**", "/settings/**").hasAuthority("Admin")
                .hasAnyAuthority("Admin", "Editor", "Salesperson")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll()
                .and()
                //for cookies to survive when app is restrted
                .rememberMe()
                      .key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                      .tokenValiditySeconds(7 * 24 * 60 * 60) //1 week  
                .and()
                .logout().permitAll();
 
        http.headers().frameOptions().sameOrigin();
 
        return http.build();
    }
 * */
