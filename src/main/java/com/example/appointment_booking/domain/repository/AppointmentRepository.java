package com.example.appointment_booking.domain.repository;

import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Query("SELECT CASE WHEN Count(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.specialist = :specialist " +
            "AND a.startDateTime < :newEnd " +
            "AND a.endDateTime > :newStart")
    boolean hasConflict(@Param("specialist") Specialist specialist,
                        @Param("newStart") LocalDateTime newStart,
                        @Param("newEnd") LocalDateTime newEnd);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.specialist.id = :specialistId " +
            "AND a.status = 'RESERVED'" +
            "AND a.startDateTime >= CURRENT_TIMESTAMP " +
            "ORDER BY a.startDateTime")
    List<Appointment> findUpcomingAppointmentsForSpecialist(@Param("specialistId") Long specialistId);
}
