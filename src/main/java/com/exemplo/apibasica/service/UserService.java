package com.exemplo.apibasica.service;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cadastro de usuário da aplicação.
     * Gera o cadastro com uma senha segura em hash.
     *
     * @param userDTO
     * @return Em caso de sucesso retorna o usuário cadastrado.
     */
    public User registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username já existe!");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash seguro
        user.setRole("USER");

        return userRepository.save(user);
    }
}
