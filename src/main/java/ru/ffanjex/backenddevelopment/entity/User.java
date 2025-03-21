package ru.ffanjex.backenddevelopment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Transient;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "confirmPassword")
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull(message = "Email can't be null")
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull(message = "Password can't be null")
    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("true")
    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled;

    @Transient
    @Size(max = 255)
    @NotNull(message = "Confirm password can't be null")
    private String confirmPassword;
}