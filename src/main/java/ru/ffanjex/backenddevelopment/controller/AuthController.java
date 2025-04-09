package ru.ffanjex.backenddevelopment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.JwtResponse;
import ru.ffanjex.backenddevelopment.dto.MedicalCardDTO;
import ru.ffanjex.backenddevelopment.dto.UserLoginDTO;
import ru.ffanjex.backenddevelopment.dto.UserRegistrationDTO;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;
import ru.ffanjex.backenddevelopment.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final MedicalCardService medicalCardService;

    @PostMapping("/register/users")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/medical_card")
    public ResponseEntity<?> registerMedicalCard(@Valid @RequestBody MedicalCardDTO dto,
                                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        MedicalCardDTO createdCard = medicalCardService.createMedicalCard(dto, token);
        return ResponseEntity.ok(createdCard);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserLoginDTO dto) {
        JwtResponse tokens = userService.authenticate(dto);
        return ResponseEntity.ok(tokens);
    }
}