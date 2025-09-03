package com.example.herzberg.repository;

import com.example.herzberg.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findAllByOrderByDateTimeDesc();
    List<TestResult> findByUsernameOrderByDateTimeDesc(String username);
}
