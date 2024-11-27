package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.Mockito.verify;

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

        Mockito.when(userService.registerUser(userDTO)).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.registerUser(userDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("success", response.getBody().get("status"));
        Assertions.assertEquals("Usuário " + userDTO.getUsername() + " cadastrado com sucesso!", response.getBody().get("message"));

        verify(userService).registerUser(userDTO);
    }

    @Test
    void deveRetornarErroQuandoUsuarioNaoForCadastrado() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("username");

        Mockito.when(userService.registerUser(userDTO))
                .thenThrow(new IllegalArgumentException("Erro ao cadastrar usuário!"));

        ResponseEntity<Map<String, Object>> response = userController.registerUser(userDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("error", response.getBody().get("status"));
        Assertions.assertEquals("Erro ao cadastrar usuário!", response.getBody().get("message"));

        verify(userService).registerUser(userDTO);
    }
}
