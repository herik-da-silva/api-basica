package com.exemplo.apibasica.config;

import com.exemplo.apibasica.filter.JwtAuthenticationFilter;
import com.exemplo.apibasica.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desabilitando CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login").permitAll() // Permitir acesso ao login
                        .requestMatchers("/api/admin").hasRole("ADMIN") // Somente ADMIN pode acessar
                        .anyRequest().authenticated() // Qualquer outra requisição precisa estar autenticada
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
