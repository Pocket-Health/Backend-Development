package ru.ffanjex.backenddevelopment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryResponse {
    private List<ChatMessage> messages;
}
