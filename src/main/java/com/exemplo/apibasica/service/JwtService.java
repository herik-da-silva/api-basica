package com.exemplo.apibasica.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Geração de token apartir do username e da role da autenticação.
     * @param username
     * @param role
     * @return Retorna token gerado.
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)              // Define o 'username' como sujeito do token
                .claim("role", role)            // Adiciona a 'role' como uma claim personalizada
                .setIssuedAt(new Date())           // Define a data de emissão como a atual
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1h de validade
                .signWith(secretKey)               // Assina o token com a chave secreta
                .compact();                        // Converte o JWT para string compactada
    }

    /**
     * Extrai a role do token da sessão.
     * @param token
     * @return Retorna a role do usuário.
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)               // Define a chave secreta usada para validar o token
                .build()
                .parseClaimsJws(token)                  // Decodifica e valida o token JWT
                .getBody()
                .get("role", String.class);          // Extrai a claim 'role'
    }

}
