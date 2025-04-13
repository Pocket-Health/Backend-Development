package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ffanjex.backenddevelopment.dto.MedicalCardDTO;
import ru.ffanjex.backenddevelopment.dto.MedicalCardRequest;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
    private final UserService userService;

    public MedicalCard getAllMedicalCards() {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        Optional<MedicalCard> medicalCards = medicalCardRepository.findByUser(user);
        return medicalCards.get();
    }

    public MedicalCard editMedicalCard(MedicalCardRequest medicalCardRequest) {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        Optional<MedicalCard> medicalCardOpt = medicalCardRepository.findByUser(user);
        if (medicalCardOpt.isEmpty()) {
            throw new IllegalArgumentException("Medical card not found");
        }

        MedicalCard medicalCard = medicalCardOpt.get();

        if (!medicalCard.getUser().equals(user)) {
            throw new IllegalArgumentException("Medical card does not belong to the current user");
        }

        medicalCard.setFullName(medicalCardRequest.getFullname());
        medicalCard.setHeight(medicalCardRequest.getHeight());
        medicalCard.setWeight(medicalCardRequest.getWeight());
        medicalCard.setBloodType(medicalCardRequest.getBlood_type());
        medicalCard.setAllergies(medicalCardRequest.getAllergies());
        medicalCard.setDiseases(medicalCardRequest.getDiseases());

        MedicalCard updatedMedicalCard = medicalCardRepository.save(medicalCard);
        return updatedMedicalCard;
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}