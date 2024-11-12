package com.exemplo.apibasica.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void deveGerarToken() {
        String username = "usuario";
        String role = "USER";

        String token = jwtService.generateToken(username, role);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairRoleDoToken() {
        String username = "usuario";
        String role = "ADMIN";
        String token = jwtService.generateToken(username, role);

        String roleExtraida = jwtService.extractRole(token);

        Assertions.assertEquals(role, roleExtraida);
    }
}
