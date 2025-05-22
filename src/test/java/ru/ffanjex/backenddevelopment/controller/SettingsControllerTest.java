package ru.ffanjex.backenddevelopment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ffanjex.backenddevelopment.dto.PasswordChangeRequest;
import ru.ffanjex.backenddevelopment.service.SettingsService;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class SettingsControllerTest {

    @Mock
    private SettingsService settingsService;

    @InjectMocks
    private SettingsController settingsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSettings_ShouldReturnNotificationStatus() {
        when(settingsService.getNotificationStatus()).thenReturn(true);
        ResponseEntity<Map<String, Boolean>> response = settingsController.getSettings();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("notification"));
        verify(settingsService).getNotificationStatus();
    }

    @Test
    void changePassword_ShouldReturnSuccessMessage() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        doNothing().when(settingsService).changePassword(request.getOldPassword(), request.getNewPassword());
        ResponseEntity<?> response = settingsController.changePassword(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", response.getBody());
        verify(settingsService).changePassword(request.getOldPassword(), request.getNewPassword());
    }

    @Test
    void enableNotification_ShouldReturnSuccessMessage() {
        doNothing().when(settingsService).setNotificationStatus(true);
        ResponseEntity<?> response = settingsController.enableNotification();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notification enables", response.getBody());
        verify(settingsService).setNotificationStatus(true);
    }

    @Test
    void disableNotification_ShouldReturnSuccessMessage() {
        doNothing().when(settingsService).setNotificationStatus(false);
        ResponseEntity<?> response = settingsController.disableNotification();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Notification disables", response.getBody());
        verify(settingsService).setNotificationStatus(false);
    }
}
