package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;

@RestController
@RequestMapping("/api/medical_card")
@RequiredArgsConstructor
public class MedicalCardController {

    private final MedicalCardService medicalCardService;

    @GetMapping("/medical_card")
    public ResponseEntity<MedicalCard> getMedicalCard() {
        MedicalCard medicalCard = medicalCardService.getMedicalCard();
        return ResponseEntity.ok(medicalCard);
    }

    @PutMapping("/edit_medical_card")
    public ResponseEntity<MedicalCard> editMedicalCard(@RequestBody MedicalCardRequest medicalCardRequest) {
        MedicalCard medicalCard = medicalCardService.editMedicalCard(medicalCardRequest);
        return ResponseEntity.ok(medicalCard);
    }
}
