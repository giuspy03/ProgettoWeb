package com.AutolavaggioDomicilio.demo.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class WasherAvailabilityRequest {
    // Getters and Setters
    private Long washerId;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

}