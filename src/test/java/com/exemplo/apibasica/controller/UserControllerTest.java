package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void deveCadastrarUsuario() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        User user = new User();
        user.setUsername(userDTO.getUsername());

        when(userService.registerUser(userDTO)).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.registerUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Usuário " + userDTO.getUsername() + " cadastrado com sucesso!", response.getBody().get("message"));

        verify(userService).registerUser(userDTO);
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoForCadastrado() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        when(userService.registerUser(userDTO))
                .thenThrow(new IllegalArgumentException("Erro ao cadastrar usuário!"));

        ResponseEntity<Map<String, Object>> response = userController.registerUser(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().get("status"));
        assertEquals("Erro ao cadastrar usuário!", response.getBody().get("message"));

        verify(userService).registerUser(userDTO);
    }
}
