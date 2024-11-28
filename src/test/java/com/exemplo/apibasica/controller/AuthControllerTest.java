package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import com.exemplo.apibasica.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveRetornarTokenQuandoCredenciaisCorretas() {
        String username = "usuario";
        String senha = "senha";
        String token = "token-jwt";

        User user = new User();
        user.setUsername(username);
        user.setPassword("senha-hash");

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(senha, user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(username, user.getRole())).thenReturn(token);

        ResponseEntity<Map<String, Object>> response = authController.login(username, senha);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(token, response.getBody().get("token"));
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoEncontrado() {
        String username = "usuario";
        String senha = "senha";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = authController.login(username, senha);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Usuário " + username + "não encontrado!", response.getBody().get("message"));
    }

    @Test
    void deveRetornarErroQuandoSenhaIncorreta() {
        String username = "usuario";
        String senha = "senha";

        User user = new User();
        user.setUsername(username);
        user.setPassword("senha-hash");

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(senha, user.getPassword())).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = authController.login(username, senha);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Senha incorreta!", response.getBody().get("message"));
    }

    @Test
    void deveEfetuarLogoutQuandoCredenciaisCorretas() {
        String validToken = "Bearer validToken123";
        doNothing().when(jwtService).invalidateToken("validToken123"); // quando o método invalidateToken for chamado no mock jwtService com o argumento "validToken123", não faça nada (doNothing)

        ResponseEntity<Map<String, Object>> response = authController.logout(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Logout realizado com sucesso!", response.getBody().get("message"));

        verify(jwtService).invalidateToken("validToken123"); // Verifica se o método foi realemnte chamado com o argumento "validToken123"
    }

    @Test
    void deveRetornarFalhaQuandoTokenNaoFornecidoOuInvalido() {
        String invalidToken = "InvalidToken";

        ResponseEntity<Map<String, Object>> response = authController.logout(invalidToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Token não fornecido ou inválido!", response.getBody().get("message"));
    }
}
