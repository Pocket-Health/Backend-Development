package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.ChatHistory;

import java.util.UUID;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, UUID> {
    ChatHistory findByUserId(UUID userId);
}
