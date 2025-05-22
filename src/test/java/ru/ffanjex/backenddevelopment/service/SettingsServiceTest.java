package ru.ffanjex.backenddevelopment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SettingsService settingsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("test@example.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldPassword");
        user.setNotificationsEnabled(true);
    }

    @Test
    void changePassword_ShouldEncodeAndSaveNewPassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String encodedOldPassword = "encodedOldPassword";
        String encodedNewPassword = "encodedNewPassword";
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(encodedOldPassword);
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches(oldPassword, encodedOldPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        settingsService.changePassword(oldPassword, newPassword);
        assertEquals(encodedNewPassword, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void getNotificationStatus_ShouldReturnCurrentStatus() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        boolean status = settingsService.getNotificationStatus();
        assertTrue(status);
        verify(userService).getUserByEmail("test@example.com");
    }

    @Test
    void setNotificationStatus_ShouldUpdateAndSaveStatus() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        settingsService.setNotificationStatus(false);
        assertFalse(settingsService.getNotificationStatus());
        verify(userRepository).save(user);
    }
}
