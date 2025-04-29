package ru.ffanjex.backenddevelopment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Value("${yandex.gpt.api-key}")
    private String apiKey;

    @Value("${yandex.gpt.model-uri}")
    private String modelUri;

    @Value("${yandex.gpt.folder-id}")
    private String folderId;

    public String askGpt(String message) {
        String apiUrl = "https://llm.api.cloud.yandex.net/foundationModels/v1/completion";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(apiUrl);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Authorization", "Api-Key " + apiKey);
            String json = String.format(
                    "{\"modelUri\":\"%s\",\"completionOptions\":{\"stream\":false,\"temperature\":0.3,\"maxTokens\":8000},\"messages\":[{\"role\":\"system\",\"text\":\"Ты медицинский помощник. Ты говоришь понятно и профессионально. Ты даёшь краткие, точные и достоверные медицинские объяснения. Ты не заменяешь врача, а помогаешь понять информацию о заболевании. Ты говоришь от лица мужчины. Твоя задача - выдавать информацию о возможной болезни, её описание, виды и причины по описанным пользователем симптомам.\"},{\"role\":\"user\",\"text\":\"%s\"}]}",
                    modelUri, message.replace("\"", "\\\"")
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
                    return extractTextFromJson(result.toString());
                }
            }
        } catch (Exception e) {
            logger.error("There was an error when requesting: ", e);
        }
        return "Request error.";
    }

    private String extractTextFromJson(String json) {
        int start = json.indexOf("\"text\":\"");
        if (start == -1) {
            return "The answer is not received.";
        }
        start += 8;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}