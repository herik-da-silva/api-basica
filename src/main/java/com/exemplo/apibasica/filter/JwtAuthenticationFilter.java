package com.exemplo.apibasica.filter;

import com.exemplo.apibasica.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization"); // Extrai o cabeçalho "Authorization"

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // Verifica se há um token e se ele começa com "Bearer "
            String token = authorizationHeader.substring(7); // Extrai o token (ignorando o prefixo "Bearer ")
            String role = jwtService.extractRole(token); // Extrai a role do token usando o JwtService

            if (role != null) { // Se a role foi encontrada, cria a autenticação no contexto do Spring Security
                // Adiciona a autenticação no contexto de segurança do Spring
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                "user",  // Pode ser um username, aqui estamos usando uma string fixa
                                null, // Credenciais não são necessárias neste caso
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)) // Autoridade/role
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto de segurança do Spring
            }
        }
        filterChain.doFilter(request, response);
    }
}
