package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import com.exemplo.apibasica.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Realiza a autenticação de um usuário.
     *
     * @param username
     * @param password
     * @return Retorna um token caso o usuário seja autenticado com sucesso.
     */

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        // Buscar o usuário pelo nome
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "message", "Usuário " + username + "não encontrado!"
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User user = userOptional.get();

        // Validar senha com hash utilizando BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "message", "Senha incorreta!"
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Gerar token JWT
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        Map<String, Object> successResponse = Map.of(
                "status", "success",
                "token", token,
                "message", "Login efetuado com sucesso!"
        );

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove o prefixo "Bearer "
            jwtService.invalidateToken(token); // Adiciona o token à blacklist

            Map<String, Object> successResponse = Map.of(
                    "status", "success",
                    "message", "Logout realizado com sucesso!"
            );

            return ResponseEntity.ok().body(successResponse);
        }
        Map<String, Object> errorResponse = Map.of(
                "status", "error",
                "message", "Token não fornecido ou inválido!"
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
