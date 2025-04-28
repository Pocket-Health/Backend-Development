package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.ffanjex.backenddevelopment.entity.ChatHistory;
import ru.ffanjex.backenddevelopment.entity.User;
import ru.ffanjex.backenddevelopment.repository.ChatHistoryRepository;

import org.springframework.http.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserService userService;

    private final String apiUrl = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";

    @Value("${yandex.gpt.api-key}")
    private String apiKey;

    @Value("${yandex.gpt.folder-id}")
    private String folderId;

    @Value("${yandex.gpt.model-uri}")
    private String modelUri;

    @Transactional
    public String sendMessage(String userMessage) {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        String gptResponse = sendToGpt(userMessage);
        saveChat(user, userMessage, gptResponse);
        return gptResponse;
    }

    private String sendToGpt(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Api-Key " + apiKey);

        Map<String, Object> requestBody = Map.of(
                "modelUri", modelUri,
                "completionOptions", Map.of(
                        "stream", false,
                        "temperature", 0.6,
                        "maxTokens", "2000"
                ),
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "text", userMessage
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");
            List<Map<String, String>> alternatives = (List<Map<String, String>>) result.get("alternatives");
            if (!alternatives.isEmpty()) {
                return alternatives.get(0).get("text");
            }
        }
        throw new RuntimeException("Failed to get a response from Yandex GPT.");
    }

    private void saveChat(User user, String userMessage, String gptResponse) {
        ChatHistory chatHistory = chatHistoryRepository.findByUser(user)
                .orElseGet(() -> {
                    ChatHistory newHistory = new ChatHistory();
                    newHistory.setUser(user);
                    newHistory.setMessages(new ArrayList<>());
                    return newHistory;
                });
        Map<String, String> messagePair = Map.of(userMessage, gptResponse);
        chatHistory.getMessages().add(messagePair);

        chatHistoryRepository.save(chatHistory);
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<Map<String, String>> getChatHistory() {
        String currentUserEmail = getCurrentUserEmail();
        User user = userService.getUserByEmail(currentUserEmail);
        return chatHistoryRepository.findByUser(user)
                .map(ChatHistory::getMessages)
                .orElse(Collections.emptyList());
    }
}
