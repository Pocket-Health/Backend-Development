package ru.ffanjex.backenddevelopment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ffanjex.backenddevelopment.dto.ChatRequestDto;
import ru.ffanjex.backenddevelopment.dto.ChatResponseDto;
import ru.ffanjex.backenddevelopment.entity.ChatHistory;
import ru.ffanjex.backenddevelopment.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "The final points for interacting with a medical chat.")
public class ChatController {
    private final ChatService chatService;

    @Operation(
            summary = "Send a message to chat",
            description = "Sends a text message to the AI model and returns the generated answer"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message processed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/send_question")
    public ChatResponseDto sendQuestion(@RequestBody ChatRequestDto request) {
        String answer = chatService.askGpt(request.getMessage());
        return new ChatResponseDto(answer);
    }

    @Operation(
            summary = "Get chat history",
            description = "Retrieve the user's latest chat history (up to 20 messages)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat history fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatHistory.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/chat_history")
    public ChatHistory getChatHistory() {
        return chatService.getChatHistory();
    }
}