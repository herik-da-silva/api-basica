package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import com.exemplo.apibasica.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

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

        Mockito.lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(senha, user.getPassword())).thenReturn(true);
        Mockito.when(jwtService.generateToken(username, user.getRole())).thenReturn(token);

        ResponseEntity<Map<String, String>> response = loginController.login(username, senha);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(token, response.getBody().get("token"));
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoEncontrado() {
        String username = "usuario";
        String senha = "senha";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = loginController.login(username, senha);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Usuário não encontrado", response.getBody().get("error"));
    }

}
