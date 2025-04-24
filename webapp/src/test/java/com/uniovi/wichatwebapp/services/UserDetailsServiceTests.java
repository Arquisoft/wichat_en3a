package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.repositories.UserRepository;
import entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("test@example.com", userDetails.getUsername());
        Assertions.assertEquals("encodedPassword", userDetails.getPassword());
        Assertions.assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository).getUserByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.getUserByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });
        verify(userRepository).getUserByEmail("nonexistent@example.com");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserEmailIsNull() {
        // Arrange
        testUser.setEmail(null);
        when(userRepository.getUserByEmail("null@example.com")).thenReturn(testUser);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("null@example.com");
        });
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserPasswordIsNull() {
        // Arrange
        testUser.setPassword(null);
        when(userRepository.getUserByEmail("nopassword@example.com")).thenReturn(testUser);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nopassword@example.com");
        });
    }

    @Test
    void loadUserByUsername_ShouldReturnEmptyAuthorities() {
        // Arrange
        when(userRepository.getUserByEmail("test@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        Assertions.assertNotNull(userDetails.getAuthorities());
        Assertions.assertEquals(0, userDetails.getAuthorities().size());
    }
}
