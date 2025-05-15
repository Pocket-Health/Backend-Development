package ru.ffanjex.backenddevelopment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.config.JwtTokenProvider;
import ru.ffanjex.backenddevelopment.dto.*;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.service.PasswordRecoveryService;
import ru.ffanjex.backenddevelopment.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication and Registration",
        description = "Endpoints for user authentication, registration, and password recovery")
public class AuthController {

    private final UserService userService;
    private final PasswordRecoveryService passwordRecoveryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Check if email is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The mail is free"),
            @ApiResponse(responseCode = "409", description = "Mail is busy")
    })
    @PostMapping("/register/users")
    public ResponseEntity<String> register(@Valid @RequestBody UserCheckEmailDTO dto) {
        User user = userService.getUserByEmail(dto.getEmail());

        if (user == null) {
            return ResponseEntity.ok("The mail is free");
        } else {
            return ResponseEntity.status(409).body("Mail is busy");
        }
    }

    @Operation(summary = "Register user with medical card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User and Medical Card created successfully"),
    })
    @PostMapping("/register/medical_card")
    public ResponseEntity<String> registerMedicalCard(@Valid @RequestBody MedicalCardDTO dto) {
        userService.registerUserWithMedicalCard(dto);
        return ResponseEntity.status(201).body("User and Medical Card created successfully");
    }

    @Operation(summary = "Login user and get JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserLoginDTO dto) {
        JwtResponse tokens = userService.authenticate(dto);
        return ResponseEntity.ok(tokens);
    }

    @Operation(summary = "Send password recovery code to email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recovery code sent to email"),
            @ApiResponse(responseCode = "500", description = "Failed to send email")
    })
    @PostMapping("/password_recovery/send_code")
    public ResponseEntity<String> sendRecoveryCode(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            String code = passwordRecoveryService.generateCode();
            passwordRecoveryService.sendRecoveryCode(emailRequest.getEmail(), code);
            return ResponseEntity.ok("Recovery code sent to email");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }

    @Operation(summary = "Check if recovery code is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Code verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid code")
    })
    @PostMapping("/password_recovery/check_code")
    public ResponseEntity<String> checkRecoveryCode(@Valid @RequestBody CodeVerificationRequest codeVerificationRequest) {
        boolean isValid = passwordRecoveryService.verifyCode(codeVerificationRequest.getEmail(), String.valueOf(codeVerificationRequest.getCode()));
        if (isValid) {
            return ResponseEntity.ok("Code verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid code");
        }
    }

    @Operation(summary = "Reset password with verified code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully")
    })
    @PostMapping("/password_recovery/password_recovery")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordRecoveryService.resetPassword(passwordResetRequest.getEmail(), passwordResetRequest.getPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtTokenProvider.isValidToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        User user = userService.getUserByEmail(email);
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
    }
}