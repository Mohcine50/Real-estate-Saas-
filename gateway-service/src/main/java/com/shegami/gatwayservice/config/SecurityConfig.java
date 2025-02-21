package com.shegami.gatwayservice.config;


import com.shegami.gatwayservice.exceptions.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtAuthFilter jwtAuthenticationFilter;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {

        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(daoAuthenticationProvider);
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .headers(headers -> headers.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .authorizeExchange(auth -> auth
                        .pathMatchers("/auth-service/**").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "*"));
        corsConfiguration.setExposedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Methods", "Access-Control-Allow-Headers"));
        corsConfiguration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;

    }

}
