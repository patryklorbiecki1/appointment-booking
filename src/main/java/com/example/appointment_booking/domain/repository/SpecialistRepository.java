package com.example.appointment_booking.domain.repository;

import com.example.appointment_booking.domain.model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialistRepository extends JpaRepository<Specialist,Long> {

    Optional<Specialist> findByUserId(Long id);
}
