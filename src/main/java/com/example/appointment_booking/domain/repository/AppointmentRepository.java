package com.example.appointment_booking.domain.repository;

import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.Specialist;
import com.example.appointment_booking.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    boolean existsBySpecialistAndDateTime(User specialist, LocalDateTime dateTime);
}
