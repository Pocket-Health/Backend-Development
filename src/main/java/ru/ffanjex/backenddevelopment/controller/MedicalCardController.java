package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.dto.MedicalCardResponse;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;

@RestController
@RequestMapping("/api/medical_card")
@RequiredArgsConstructor
public class MedicalCardController {

    private final MedicalCardService medicalCardService;

    @GetMapping("/medical_card")
    public ResponseEntity<MedicalCardResponse> getMedicalCard() {
        MedicalCardResponse medicalCard = medicalCardService.getMedicalCard();
        System.out.println("Returning DTO: " + medicalCard);
        return ResponseEntity.ok(medicalCard);
    }

    @PutMapping("/edit_medical_card")
    public ResponseEntity<MedicalCardResponse> editMedicalCard(@RequestBody MedicalCardRequest medicalCardRequest) {
        MedicalCardResponse medicalCard = medicalCardService.editMedicalCard(medicalCardRequest);
        return ResponseEntity.ok(medicalCard);
    }
}