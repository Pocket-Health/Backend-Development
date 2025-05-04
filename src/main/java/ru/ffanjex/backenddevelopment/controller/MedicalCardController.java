package ru.ffanjex.backenddevelopment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.dto.MedicalCardResponse;
import ru.ffanjex.backenddevelopment.service.MedicalCardService;

@RestController
@RequestMapping("/api/medical_card")
@RequiredArgsConstructor
@Tag(name = "Medical Card", description = "Operations related to user's medical card")
public class MedicalCardController {

    private final MedicalCardService medicalCardService;

    @Operation(summary = "Get medical card", description = "Returns the current user's medical card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved medical card",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalCardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/medical_card")
    public ResponseEntity<MedicalCardResponse> getMedicalCard() {
        MedicalCardResponse medicalCard = medicalCardService.getMedicalCard();
        System.out.println("Returning DTO: " + medicalCard);
        return ResponseEntity.ok(medicalCard);
    }

    @Operation(summary = "Edit medical card", description = "Updates the current user's medical card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated medical card",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalCardResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/edit_medical_card")
    public ResponseEntity<MedicalCardResponse> editMedicalCard(@RequestBody MedicalCardRequest medicalCardRequest) {
        MedicalCardResponse medicalCard = medicalCardService.editMedicalCard(medicalCardRequest);
        return ResponseEntity.ok(medicalCard);
    }
}