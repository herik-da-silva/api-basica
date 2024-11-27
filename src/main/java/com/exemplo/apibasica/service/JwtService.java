package com.exemplo.apibasica.service;

import com.exemplo.apibasica.repository.TokenBlacklistRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}") // Injeta o valor da variável de ambiente
    private String secretKeyString;

    @Autowired
    private TokenBlacklistRepository blacklistRepository;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Converte a string da chave secreta em um objeto SecretKey
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

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
                .setExpiration(new Date(System.currentTimeMillis() + 14400000)) // 4h de validade
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

    /**
     * Extrai o username do token da sessão.
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Define a chave secreta usada para validar o token
                .build()
                .parseClaimsJws(token)    // Decodifica e valida o token JWT
                .getBody()
                .getSubject();           // Retorna o "subject", que é o username no token
    }


    /**
     * Verifica se um token é valido na blacklist
     * além de remover tokens expirados.
     * @param token
     * @return
     */
    public boolean isTokenValid(String token) {
        try {
            blacklistRepository.deleteExpiredTokens(); // Remove tokens expirados da blacklist
            if (blacklistRepository.isTokenBlacklisted(token)) {
                return false; // Token está na blacklist
            }
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token); // Valida o token
            return true;
        } catch (JwtException e) {
            return false; // Token inválido
        }
    }

    /**
     * Adiciona um token à blacklist, marcando-o como inválido.
     * @param token
     */
    public void invalidateToken(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        blacklistRepository.addTokenToBlacklist(token, expirationDate);
    }

}
