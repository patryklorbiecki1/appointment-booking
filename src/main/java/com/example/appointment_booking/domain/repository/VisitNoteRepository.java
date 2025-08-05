package com.example.appointment_booking.domain.repository;

import com.example.appointment_booking.domain.model.VisitNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitNoteRepository extends JpaRepository<VisitNote,Long> {
    Optional<VisitNote> findByAppointmentId(Long appointmentId);
}
