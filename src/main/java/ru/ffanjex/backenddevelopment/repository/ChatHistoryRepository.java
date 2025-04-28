package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.ChatHistory;
import ru.ffanjex.backenddevelopment.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, UUID> {
    Optional<ChatHistory> findByUser(User user);
}
