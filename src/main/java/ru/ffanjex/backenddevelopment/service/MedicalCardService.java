package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.dto.MedicalCardResponse;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;

@Service
@RequiredArgsConstructor
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
    private final UserService userService;

    public MedicalCardResponse getMedicalCard() {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        MedicalCard medicalCard = medicalCardRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Medical card not found"));
        return mapToResponse(medicalCard);
    }

    public MedicalCardResponse editMedicalCard(MedicalCardRequest request) {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        MedicalCard medicalCard = medicalCardRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Medical card not found"));

        if (!medicalCard.getUser().equals(user)) {
            throw new IllegalArgumentException("Medical card does not belong to the current user");
        }

        medicalCard.setFullName(request.getFullname());
        medicalCard.setHeight(request.getHeight());
        medicalCard.setWeight(request.getWeight());
        medicalCard.setBloodType(request.getBlood_type());
        medicalCard.setAllergies(request.getAllergies());
        medicalCard.setDiseases(request.getDiseases());

        medicalCardRepository.save(medicalCard);
        return mapToResponse(medicalCard);
    }

    private MedicalCardResponse mapToResponse(MedicalCard card) {
        MedicalCardResponse response = new MedicalCardResponse();
        response.setFullName(card.getFullName());
        response.setHeight(card.getHeight());
        response.setWeight(card.getWeight());
        response.setBloodType(card.getBloodType());
        response.setAllergies(card.getAllergies());
        response.setDiseases(card.getDiseases());
        return response;
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}