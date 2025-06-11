package com.example.appointment_booking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private User patient;
    @ManyToOne(fetch = FetchType.LAZY)
    private User specialist;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
