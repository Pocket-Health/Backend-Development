package ru.ffanjex.backenddevelopment.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ffanjex.backenddevelopment.dto.ChatMessage;
import ru.ffanjex.backenddevelopment.dto.MessageContent;
import ru.ffanjex.backenddevelopment.entity.*;
import ru.ffanjex.backenddevelopment.repository.ChatHistoryRepository;
import ru.ffanjex.backenddevelopment.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Value("${yandex.gpt.api-key}")
    private String apiKey;

    @Value("${yandex.gpt.model-uri}")
    private String modelUri;

    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    public String askGpt(String userMessage) {
        String apiUrl = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(apiUrl);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Api-Key " + apiKey);
            String json = String.format(
                    "{\"modelUri\":\"%s\",\"completionOptions\":{\"stream\":false,\"temperature\":0.3,\"maxTokens\":8000},\"messages\":[{\"role\":\"system\",\"text\":\"Ты медицинский помощник. Ты говоришь понятно и профессионально. Ты даёшь краткие, точные и достоверные медицинские объяснения. Ты не заменяешь врача, а помогаешь понять информацию о заболевании. Ты говоришь от лица мужчины. Твоя задача - выдавать информацию о возможной болезни, её описание, виды и причины по описанным пользователем симптомам.\"},{\"role\":\"user\",\"text\":\"%s\"}]}",
                    modelUri, userMessage.replace("\"", "\\\"")
            );
            request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    String gptAnswer = extractTextFromJson(result.toString());

                    saveToChatHistory(userMessage, gptAnswer);
                    return gptAnswer;
                }
            }
        } catch (Exception e) {
            logger.error("There was an error when requesting: ", e);
        }
        return "Ошибка при запросе.";
    }

    private void saveToChatHistory(String userMessage, String gptAnswer) {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));

        ChatHistory history = chatHistoryRepository.findByUserId(user.getId());
        if (history == null) {
            history = new ChatHistory();
            history.setUser(user);
            history.setMessages(new ArrayList<>());
        }

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(new MessageContent(userMessage));
        chatMessage.setMessage2(new MessageContent(gptAnswer));

        history.getMessages().add(chatMessage);

        if (history.getMessages().size() > 20) {
            history.getMessages().remove(0);
        }

        chatHistoryRepository.save(history);
    }

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String extractTextFromJson(String json) {
        int start = json.indexOf("\"text\":\"");
        if (start == -1) {
            return "Ответ не получен.";
        }
        start += 8;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    public ChatHistory getChatHistory() {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));
        return chatHistoryRepository.findByUserId(user.getId());
    }
}