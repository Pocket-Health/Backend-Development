package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.ChatRequestDto;
import ru.ffanjex.backenddevelopment.dto.ChatResponseDto;
import ru.ffanjex.backenddevelopment.service.ChatService;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send_question")
    public ChatResponseDto sendQuestion(@RequestBody ChatRequestDto request) {
        String answer = chatService.askGpt(request.getMessage());
        return new ChatResponseDto(answer);
    }
}
