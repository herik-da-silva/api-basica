package com.exemplo.apibasica.service;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveRegistrarUsuario() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("novoUsuario");
        userDTO.setPassword("senha");

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("senha-hash");

        User savedUser = new User();
        savedUser.setUsername(userDTO.getUsername());
        savedUser.setPassword("senha-hash");
        savedUser.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User user = userService.registerUser(userDTO);

        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals("senha-hash", user.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveRetornarErroQuandoUsernameExistente() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("usuarioExistente");

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));
    }
}
