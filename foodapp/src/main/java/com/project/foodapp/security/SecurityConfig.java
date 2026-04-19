package com.project.foodapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public APIs
                        .requestMatchers("/users/login", "/users").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Food items (public)
                        .requestMatchers("/items/**").permitAll()

                        // 🔥 USER APIs
                        .requestMatchers("/orders").hasAnyRole("USER", "ADMIN")

                        // 🔥 ADMIN APIs
                        .requestMatchers("/orders/all").hasRole("ADMIN")
                        .requestMatchers("/orders/*/status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/restaurants").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/restaurants/**").permitAll()


                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}