package com.exemplo.apibasica.controller;

import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import com.exemplo.apibasica.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Criação do PasswordEncoder
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        // Buscar o usuário pelo nome
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário não encontrado"));
        }

        User user = userOptional.get();

        // Validar senha com hash utilizando BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Senha incorreta"));
        }

        // Gerar token JWT
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
