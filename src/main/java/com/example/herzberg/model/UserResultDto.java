package com.example.herzberg.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResultDto {
    private String username;
    private LocalDateTime dateTime;
    private int financialMotives;
    private int recognition;
    private int managementAttitude;
    private int teamwork;
    private int responsibility;
    private int career;
    private int achievements;
    private int workContent;
}
