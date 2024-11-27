package com.exemplo.apibasica.filter;

import com.exemplo.apibasica.service.JwtService;
import io.jsonwebtoken.JwtException;
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

/**
 * Validação de token.
 */
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

            try {
                if (jwtService.isTokenValid(token)) { // Verifica se o token é válido
                    String username = jwtService.extractUsername(token); // Extrai o username
                    String role = jwtService.extractRole(token); // Extrai a role do token usando o JwtService

                    if (role != null) { // Se a role foi encontrada, cria a autenticação no contexto do Spring Security
                        // Adiciona a autenticação no contexto de segurança do Spring
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null, // Credenciais não são necessárias neste caso
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)) // Autoridade/role
                                );

                        SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto de segurança do Spring
                    }
                } else {
                    throw new JwtException("Token inválido!");
                }
            } catch (JwtException e) {
                // Configura a resposta 401 (Unauthorized) quando o token for inválido
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                String jsonResponse = "{"
                        + "\"status\": \"error\","
                        + "\"message\": \"Unauthorized: " + e.getMessage() + "\""
                        + "}";

                response.getWriter().write(jsonResponse);
                return; // Finaliza o filtro e impede que prossiga para o próximo
            }
        }
        filterChain.doFilter(request, response);
    }
}
