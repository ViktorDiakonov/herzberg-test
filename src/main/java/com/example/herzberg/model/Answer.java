package com.example.herzberg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String question;
    private int score;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); // создаем timestamp если его нет
        }
        return createdAt;
    }
}
