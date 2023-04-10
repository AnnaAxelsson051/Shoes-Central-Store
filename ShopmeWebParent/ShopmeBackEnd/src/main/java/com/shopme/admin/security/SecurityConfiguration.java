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
    
       http.authorizeRequests()
       //Admin can access users, settings, countries, states and everything after that
               .antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
               .antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
                
               .antMatchers("/products/new**", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
               
               .antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                   .hasAnyAuthority("Admin", "Editor", "Salesperson")
               
               //Prod listing page, url for products and prod details, url for pagination
               .antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
                   .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
               
                   //Everything bout products
               .antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
               .anyRequest().authenticated()
               .and()
               .formLogin()
               .loginPage("/login")
                   .usernameParameter("email")
                   .permitAll()
               .and().logOut().permitAll()
               .and()
                    .rememberMe()
                     .key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                     .tokenValiditySeconds(7 * 24 * 60 * 60) //1 week  
               .and()
               .logout().permitAll();

       http.headers().frameOptions().sameOrigin();

       return http.build();
   }
  
   
 
    //Ignoring authentication for images js and webjars directory
    //So can be displayed when not logged in
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
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
