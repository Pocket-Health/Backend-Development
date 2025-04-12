package ru.ffanjex.backenddevelopment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.*;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
}