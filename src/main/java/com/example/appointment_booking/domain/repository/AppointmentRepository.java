package com.example.appointment_booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
}
