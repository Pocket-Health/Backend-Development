package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ffanjex.backenddevelopment.dto.MedicalCardDTO;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
    private final UserRepository userRepository;

    @Transactional
    public MedicalCardDTO createMedicalCard(MedicalCardDTO dto, User user) {
        MedicalCard medicalCard = new MedicalCard();
        medicalCard.setUser(user);
        medicalCard.setFullName(dto.getFullName());
        medicalCard.setHeight(dto.getHeight());
        medicalCard.setWeight(dto.getWeight());
        medicalCard.setBloodType(dto.getBloodType());
        medicalCard.setAllergies(dto.getAllergies());
        medicalCard.setDiseases(dto.getDiseases());
        medicalCardRepository.save(medicalCard);
        return dto;
    }
}