package com.example.appointment_booking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specialist {

    @Id
    @GeneratedValue
    private Long id;
    private String specialization;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",unique = true)
    private User user;
}
