package ru.ffanjex.backenddevelopment.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.*;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.service.PasswordRecoveryService;
import ru.ffanjex.backenddevelopment.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/register/users")
    public ResponseEntity<String> register(@Valid @RequestBody UserCheckEmailDTO dto) {
        User user = userService.getUserByEmail(dto.getEmail());

        if (user == null) {
            return ResponseEntity.ok("The mail is free");
        } else {
            return ResponseEntity.status(409).body("Mail is busy");
        }
    }

    @PostMapping("/register/medical_card")
    public ResponseEntity<String> registerMedicalCard(@Valid @RequestBody MedicalCardDTO dto) {
        userService.registerUserWithMedicalCard(dto);
        return ResponseEntity.status(201).body("User and Medical Card created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserLoginDTO dto) {
        JwtResponse tokens = userService.authenticate(dto);
        return ResponseEntity.ok(tokens);
    }

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

    @PostMapping("/password_recovery/check_code")
    public ResponseEntity<String> checkRecoveryCode(@Valid @RequestBody CodeVerificationRequest codeVerificationRequest) {
        boolean isValid = passwordRecoveryService.verifyCode(codeVerificationRequest.getEmail(), String.valueOf(codeVerificationRequest.getCode()));
        if (isValid) {
            return ResponseEntity.ok("Code verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid code");
        }
    }

    @PostMapping("/password_recovery/password_recovery")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordRecoveryService.resetPassword(passwordResetRequest.getEmail(), passwordResetRequest.getPassword());
        return ResponseEntity.ok("Password reset successfully");
    }
}