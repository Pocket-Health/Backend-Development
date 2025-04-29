package ru.ffanjex.backenddevelopment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ChatResponseGpt {
    private String message;
}
