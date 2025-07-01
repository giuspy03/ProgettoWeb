package com.AutolavaggioDomicilio.demo.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean //bean aggiunto dopo
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/public/**", "/api/user/register").permitAll();
                    auth.requestMatchers("/api/user/testBackend").hasRole("washer");
                    auth.requestMatchers("/api/user/me").hasRole("washer");
                    auth.requestMatchers("/api/user/promote/washer").hasAnyRole("admin", "superUser");
                    auth.requestMatchers("/api/user/promote/admin").hasRole("superUser");
                    auth.requestMatchers("/api/user/revoke/washer").hasAnyRole("admin", "superUser");
                    auth.requestMatchers("/api/user/revoke/admin").hasRole("superUser");
                    auth.requestMatchers("/api/user/availability/washer").hasAnyRole("washer", "admin", "superUser");
                    auth.requestMatchers("/api/user/appointments/washer").hasAnyRole("washer", "admin", "superUser");
                    auth.requestMatchers("/api/user/**").hasRole("user");
                    auth.anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(KeycloakRealmRoleConverter.jwtAuthenticationConverter()))
                )/*
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )*/;

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Solo il frontend dev
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true); // Essenziale con cookie/session/token

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
