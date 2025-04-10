package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ffanjex.backenddevelopment.dto.MedicalCardDTO;
import ru.ffanjex.backenddevelopment.entity.MedicalCard;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.MedicalCardRepository;


@Service
@RequiredArgsConstructor
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
}