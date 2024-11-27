package com.exemplo.apibasica.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest { // TODO CORRIGIR ERROS

    @InjectMocks
    private JwtService jwtService;

    void deveGerarToken() {
        String username = "usuario";
        String role = "USER";

        String token = jwtService.generateToken(username, role);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }

    void deveExtrairRoleDoToken() {
        String username = "usuario";
        String role = "ADMIN";
        String token = jwtService.generateToken(username, role);

        String roleExtraida = jwtService.extractRole(token);

        Assertions.assertEquals(role, roleExtraida);
    }
}
