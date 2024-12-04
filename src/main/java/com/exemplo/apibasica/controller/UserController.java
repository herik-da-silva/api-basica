package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Cadastro de novos usuários através do request.
     *
     * @param userDTO Objeto utilizado como paramêtro para transportar os dados do cadastro.
     * @return Retorno de status identificando a execução da operação.
     */

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            User newUser = userService.registerUser(userDTO);

            Map<String, Object> successResponse = Map.of(
                    "status", "success",
                    "message", "Usuário " + newUser.getUsername() + " cadastrado com sucesso!"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "message", e.getMessage() // Personaliza o motivo do erro
            );

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
