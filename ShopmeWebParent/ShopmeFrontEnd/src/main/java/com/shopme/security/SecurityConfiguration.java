package com.shopme.security;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//import com.shopme.admin.security.ShopmeUserDetailsService;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.DatabaseLoginSuccessHandler;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;


@Configuration
public class SecurityConfiguration {
 
	@Autowired 
	private CustomerOAuth2UserService oAuth2UserService;
	@Autowired 
	private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginHandler;
	
	   @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
 
   //Only users page requiring log in with email
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
       //http.authorizeRequests()
	   http.authorizeHttpRequests()
       .requestMatchers("/account_details", "/update_account_details", //Auth required, must log in to view
    		   "/cart", "/address_book/**", "/checkout", "/place_order", "/process_paypal_order").authenticated() //** means everything after..
       .anyRequest().permitAll()
       .and()
       .formLogin()
       .loginPage("/login")
            .usernameParameter("email")
            .successHandler(databaseLoginHandler)
            .permitAll()
       .and()
       .oauth2Login()
            .loginPage("/login")
            .userInfoEndpoint()
            .userService(oAuth2UserService)
            .and()
            .successHandler(oauth2LoginHandler)
       .and()
       .logout().permitAll()
       .and()
       .rememberMe()
           .key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
           .tokenValiditySeconds(14 * 24 * 60 * 60) //2 weeks  
       .and() //For creation of session when necessary
           .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        ;
       
	   return http.build();
              
   }
  
   
 
    //Ignoring authentication for images js and webjars directory
    //So can be displayed when not logged in
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
    	 return new CustomerUserDetailsService();
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
 
}


