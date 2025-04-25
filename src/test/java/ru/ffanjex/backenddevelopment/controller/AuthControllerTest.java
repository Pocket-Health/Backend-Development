package ru.ffanjex.backenddevelopment.controller;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ffanjex.backenddevelopment.dto.*;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.service.PasswordRecoveryService;
import ru.ffanjex.backenddevelopment.service.UserService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordRecoveryService passwordRecoveryService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_WhenEmailIsFree_ShouldReturnOk() {
        String email = "free@mail.com";
        UserCheckEmailDTO dto = new UserCheckEmailDTO(email);
        when(userService.getUserByEmail(email)).thenReturn(null);
        ResponseEntity<String> response = authController.register(dto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The mail is free", response.getBody());
        verify(userService).getUserByEmail(email);
    }

    @Test
    void register_WhenEmailIsBusy_ShouldReturnConflict() {
        String email = "test@example.com";
        UserCheckEmailDTO dto = new UserCheckEmailDTO(email);
        User existingUser = new User();
        existingUser.setEmail(email);
        when(userService.getUserByEmail(email)).thenReturn(existingUser);
        ResponseEntity<String> response = authController.register(dto);
        assertEquals(409, response.getStatusCode().value());
        assertEquals("Mail is busy", response.getBody());
        verify(userService).getUserByEmail(email);
    }

    @Test
    void registerMedicalCard_ShouldReturnCreated() {
        MedicalCardDTO dto = new MedicalCardDTO();
        ResponseEntity<String> response = authController.registerMedicalCard(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User and Medical Card created successfully", response.getBody());
        verify(userService).registerUserWithMedicalCard(dto);
    }

    @Test
    void login_ShouldReturnJwtTokens() {
        UserLoginDTO dto = new UserLoginDTO("test@example.com", "password123");
        JwtResponse expectedResponse = new JwtResponse("accessTokenExample", "refreshTokenExample");
        when(userService.authenticate(dto)).thenReturn(expectedResponse);
        ResponseEntity<JwtResponse> response = authController.login(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService).authenticate(dto);
    }

    @Test
    void sendRecoveryCode_ShouldReturnOk() throws MessagingException {
        String email = "test@example.com";
        String generatedCode = "123456";
        EmailRequest request = new EmailRequest(email);
        when(passwordRecoveryService.generateCode()).thenReturn(generatedCode);
        doNothing().when(passwordRecoveryService).sendRecoveryCode(email, generatedCode);
        ResponseEntity<String> response = authController.sendRecoveryCode(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Recovery code sent to email", response.getBody());
        verify(passwordRecoveryService).generateCode();
        verify(passwordRecoveryService).sendRecoveryCode(email, generatedCode);
    }

    @Test
    void sendRecoveryCode_WhenMessagingException_ShouldReturnServerError() throws MessagingException {
        String email = "test@example.com";
        String generatedCode = "123456";
        EmailRequest emailRequest = new EmailRequest(email);
        when(passwordRecoveryService.generateCode()).thenReturn(generatedCode);
        doThrow(new MessagingException("Error")).when(passwordRecoveryService).sendRecoveryCode(email, generatedCode);
        ResponseEntity<String> response = authController.sendRecoveryCode(emailRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to send email", response.getBody());
    }

    @Test
    void checkRecoveryCode_WhenValid_ShouldReturnOk() throws MessagingException {
        String email = "test@example.com";
        int code = 123456;
        CodeVerificationRequest codeVerificationRequest = new CodeVerificationRequest(email, code);
        when(passwordRecoveryService.verifyCode(email, String.valueOf(code))).thenReturn(true);
        ResponseEntity<String> response = authController.checkRecoveryCode(codeVerificationRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Code verified successfully", response.getBody());
    }

    @Test
    void checkRecoveryCode_WhenInvalid_ShouldReturnBadRequest() {
        String email = "test@example.com";
        int code = 123456;
        CodeVerificationRequest codeVerificationRequest = new CodeVerificationRequest(email,
                code);
        when(passwordRecoveryService.verifyCode(email, String.valueOf(code)))
                .thenReturn(false);
        ResponseEntity<String> response = authController.checkRecoveryCode(codeVerificationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid code", response.getBody());
    }

    @Test
    void resetPassword_ShouldReturnOk() {
        String email = "test@example.com";
        String newPassword = "newPassword123";
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(email, newPassword);
        doNothing().when(passwordRecoveryService).resetPassword(email, newPassword);
        ResponseEntity<String> response = authController.resetPassword(passwordResetRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successfully", response.getBody());
        verify(passwordRecoveryService).resetPassword(email, newPassword);
    }
}
