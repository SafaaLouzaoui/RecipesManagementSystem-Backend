package com.gestion.recettes.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtAuthentificationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/recettes/lireTous",
                        "/api/v1/recettes/lire/**",
                        "/api/v1/categories/lireTous",
                        "/api/v1/categories/lire/**",
                        "/api/v1/ingredients/lireTous",
                        "/api/v1/ingredients/lire/**",
                        "/api/v1/utilisateurs/lire/**",
                        "/api/v1/recettes/recettesByCategorie/**",
                        "/api/v1/recettes/search",
                        "/api/v1/motsCles/lireTous",
                        "/api/v1/motsCles/lire/**",
                        "/api/v1/medias/lireTous",
                        "/api/v1/medias/lire/**"        
                        )
                .permitAll()
        
                .requestMatchers(PUT, "/api/v1/recettes/modifier/**").hasAnyRole("ADMIN")
                .requestMatchers(DELETE, "/api/v1/recettes/supprimer/**").hasAnyRole("ADMIN")
        
                .requestMatchers(POST, "/api/v1/recettes/creer").hasAnyRole("USER")
                .requestMatchers(PUT, "/api/v1/recettes/mien/modifier/**").hasAnyRole("USER")
                .requestMatchers(DELETE, "/api/v1/recettes/mien/supprimer/**").hasAnyRole("USER")
                .requestMatchers(POST,"/api/v1/categories/creer").hasAnyRole("ADMIN")
                .requestMatchers(PUT,"/api/v1/categories/modifier/**").hasAnyRole("ADMIN")
                .requestMatchers(DELETE,"/api/v1/categories/supprimer/**").hasAnyRole("ADMIN")
        
                .requestMatchers(POST,"/api/v1/ingredients/creer").hasAnyRole("ADMIN")
                .requestMatchers(PUT,"/api/v1/ingredients/modifier/**").hasAnyRole("ADMIN")
                .requestMatchers(DELETE,"/api/v1/ingredients/supprimer/**").hasAnyRole("ADMIN")
        
                
                .requestMatchers(PUT,"/api/v1/utilisateurs/modifier/**").hasAnyRole("ADMIN","USER")
                .requestMatchers(DELETE,"/api/v1/utilisateurs/supprimer/**").hasAnyRole("ADMIN")
        
                .requestMatchers(POST,"/api/v1/motsCles/creer").hasAnyRole("ADMIN")
                .requestMatchers(PUT,"/api/v1/motsCles/modifier/**").hasAnyRole("ADMIN")
                .requestMatchers(DELETE,"/api/v1/motsCles/supprimer/**").hasAnyRole("ADMIN")
                
                
                
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        ;
        
        return http.build();
    }
}
