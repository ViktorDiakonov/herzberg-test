package com.example.herzberg.repository;

import com.example.herzberg.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByUsername(String username);

    @Query("SELECT DISTINCT a.username FROM Answer a")
    List<String> findDistinctUsernames();

    // Новый метод для получения ответов с сортировкой
    @Query("SELECT a FROM Answer a WHERE a.username = :username ORDER BY a.createdAt DESC")
    List<Answer> findByUsernameOrderByCreatedAtDesc(String username);
}

