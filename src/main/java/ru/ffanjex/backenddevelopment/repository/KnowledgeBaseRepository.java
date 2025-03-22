package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.KnowledgeBase;

import java.util.UUID;

public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBase, UUID> {
}
