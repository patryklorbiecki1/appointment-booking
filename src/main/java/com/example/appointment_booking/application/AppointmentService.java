package com.example.appointment_booking.application;

import com.example.appointment_booking.domain.exception.ForbiddenOperationException;
import com.example.appointment_booking.domain.exception.SlotAlreadyTakenException;
import com.example.appointment_booking.domain.exception.SpecialistNotFoundException;
import com.example.appointment_booking.domain.model.*;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.SpecialistRepository;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SpecialistRepository specialistRepository;

    public Appointment bookAppointment(AppointmentRequest request, User patient){
        Specialist specialist = specialistRepository.findByUserId(request.getSpecialistId())
                .orElseThrow(() -> new SpecialistNotFoundException("Specialist not found"));

        if(patient.getRole()!= Role.PATIENT){
            throw new ForbiddenOperationException("Only patients can book appointments");
        }
        boolean isTaken = appointmentRepository.hasConflict(specialist,
                request.getDateTime(),
                request.getDateTime().plusMinutes(request.getDuration()));
        if (isTaken) {
            throw new SlotAlreadyTakenException("This appointment slot is already taken");
        }
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setStartDateTime(request.getDateTime());
        appointment.setEndDateTime(request.getDateTime().plusMinutes(request.getDuration()));
        appointment.setSpecialist(specialist);
        appointment.setStatus(AppointmentStatus.RESERVED);

        return appointmentRepository.save(appointment);
    }
}
