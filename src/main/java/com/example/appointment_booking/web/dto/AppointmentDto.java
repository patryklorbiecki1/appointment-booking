package com.example.appointment_booking.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AppointmentDto {
        private Long specialistId;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String visitNoteContent;
}
