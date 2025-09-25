package com.example.herzberg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private double motivationAvg;

    @Column(nullable = false)
    private double hygieneAvg;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public TestResult() {}

}
