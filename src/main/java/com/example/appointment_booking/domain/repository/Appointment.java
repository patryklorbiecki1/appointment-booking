package com.example.appointment_booking.domain.repository;

import com.example.appointment_booking.domain.model.AppointmentStatus;
import com.example.appointment_booking.domain.model.Specialist;
import com.example.appointment_booking.domain.model.User;
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
    private Specialist specialist;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
