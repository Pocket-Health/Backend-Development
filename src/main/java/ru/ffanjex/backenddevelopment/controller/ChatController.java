package ru.ffanjex.backenddevelopment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.ChatRequestDto;
import ru.ffanjex.backenddevelopment.dto.ChatResponseGpt;
import ru.ffanjex.backenddevelopment.service.ChatService;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/send_question")
    public ChatResponseGpt sendQuestion(@RequestBody ChatRequestDto request) {
        String answer = chatService.askGpt(request.getMessage());
        return new ChatResponseGpt(answer);
    }
}
