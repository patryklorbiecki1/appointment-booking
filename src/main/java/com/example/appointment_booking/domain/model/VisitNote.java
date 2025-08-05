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
public class VisitNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="appointment_id",nullable = false,unique = true)
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}
