package ru.ffanjex.backenddevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ffanjex.backenddevelopment.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
