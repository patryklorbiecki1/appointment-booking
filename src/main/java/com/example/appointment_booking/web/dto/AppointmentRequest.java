package com.example.appointment_booking.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long specialistId;
    private LocalDateTime dateTime;
    private Integer duration;
}