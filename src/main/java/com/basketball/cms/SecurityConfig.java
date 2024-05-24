/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.basketball.cms;

/**
 *
 * @author limziyang
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    //Configure an in-memory user details service with a predefined user
    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        // Creates a user with username "HokkienMee" and password "HokkienMeeIsRed2024"
        UserDetails user = User.withUsername("HokkienMee").password(passwordEncoder().encode("HokkienMeeIsRed2024"))
                .authorities("read").build();
        userDetailsService.createUser(user);
        return userDetailsService;
    }
    
    //Configure a BCrypt password encoder
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //Configure the security filter chain
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// Disables CSRF protection
                .authorizeRequests()
                // Allows unrestricted access to the root, players API, locations, and player profiles
                .requestMatchers("/").permitAll() 
                .requestMatchers("/players/api/**").permitAll()
                .requestMatchers("/locations/**").permitAll()
                .requestMatchers("/players/profile/**").permitAll()
                // Requires authentication for all other player-related and team-related endpoints
                .requestMatchers("/players/**").authenticated()
                .requestMatchers("/team/**").authenticated()
                .and()
                .formLogin()
                // Specifies the login page URL and the default success URL after login
                .loginPage("/login") 
                .defaultSuccessUrl("/players", true)
                .permitAll();
        http.formLogin();
        return http.build();
    }

}
