package com.exemplo.apibasica.service;

import com.exemplo.apibasica.dto.UserDTO;
import com.exemplo.apibasica.model.User;
import com.exemplo.apibasica.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

        Mockito.when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("senha-hash");

        User savedUser = new User();
        savedUser.setUsername(userDTO.getUsername());
        savedUser.setPassword("senha-hash");
        savedUser.setRole("USER");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User user = userService.registerUser(userDTO);

        Assertions.assertEquals(userDTO.getUsername(), user.getUsername());
        Assertions.assertEquals("senha-hash", user.getPassword());
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void deveRetornarErroQuandoUsernameExistente() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("usuarioExistente");

        Mockito.when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(userDTO));
    }
}
