package com.exemplo.apibasica.service;

import com.exemplo.apibasica.repository.TokenBlacklistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private TokenBlacklistRepository blacklistRepository;

    private final String secretKeyString = "your-secret-key-that-is-at-least-32-bytes-long";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecretKeyString(); // Usa reflexão para atribuir o valor da variável secretKeyString ao campo privado secretKeyString da classe JwtService.
        jwtService.init(); // Chama o método init da JwtService, que converte o valor de secretKeyString em um objeto SecretKey.
    }

    /**
     * Método utilizado para simular o valor da propriedade @Value("${jwt.secret}") utilizando reflexão
     * que é injetada automaticamente no campo secretKeyString pela anotação @Value no Spring
     */
    private void mockSecretKeyString() {
        // Usa reflexão para configurar o campo privado
        try {
            var field = JwtService.class.getDeclaredField("secretKeyString");  // Obtém a definição do campo privado 'secretKeyString' na classe JwtService
            field.setAccessible(true); // Define o campo como acessível (mesmo sendo privado)
            field.set(jwtService, secretKeyString); // Atribui o valor da variável local 'secretKeyString' ao campo da instância 'jwtService'
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deveGerarToken() {
        String username = "usuario";
        String role = "USER";

        String token = jwtService.generateToken(username, role);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairRoleDoToken() {
        String username = "usuario";
        String role = "ADMIN";
        String token = jwtService.generateToken(username, role);

        String roleExtraida = jwtService.extractRole(token);

        assertEquals(role, roleExtraida);
    }

    @Test
    void deveExtrairUsernameDoToken() {
        String username = "usuario";
        String role = "ADMIN";
        String token = jwtService.generateToken(username, role);

        String usernameExtraida = jwtService.extractUsername(token);

        assertEquals(username, usernameExtraida);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String token = jwtService.generateToken("testUser", "ROLE_USER");

        when(blacklistRepository.isTokenBlacklisted(token)).thenReturn(false);

        boolean isValid = jwtService.isTokenValid(token);

        assertTrue(isValid);
        verify(blacklistRepository).deleteExpiredTokens();
        verify(blacklistRepository).isTokenBlacklisted(token);
    }

    @Test
    void testIsTokenValid_BlacklistedToken() {
        String token = jwtService.generateToken("testUser", "ROLE_USER");

        when(blacklistRepository.isTokenBlacklisted(token)).thenReturn(true);

        boolean isValid = jwtService.isTokenValid(token);

        assertFalse(isValid);
        verify(blacklistRepository).deleteExpiredTokens();
        verify(blacklistRepository).isTokenBlacklisted(token);
    }

    @Test
    void testInvalidateToken() {
        String token = jwtService.generateToken("testUser", "ROLE_USER"); // Cria um token com 4 horas de validade

        jwtService.invalidateToken(token); // Simula a execução do método a ser testado

        // Verifica se o método 'addTokenToBlacklist' foi chamado no repositório com:
        // - o token gerado
        // - qualquer valor de data (a data é extraída automaticamente no método)
        verify(blacklistRepository).addTokenToBlacklist(eq(token), any(Date.class));
    }
}
