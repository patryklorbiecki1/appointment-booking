package com.example.appointment_booking.application;

import com.example.appointment_booking.domain.exception.*;
import com.example.appointment_booking.domain.model.*;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.SpecialistRepository;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found"));

        if(appointment.getStatus()!=AppointmentStatus.RESERVED){
            throw new InvalidAppointmentStatusException("Only a scheduled visit can be cancelled");
        }
        if(appointment.getStartDateTime().isBefore(LocalDateTime.now())){
            throw new PastAppointmentCancellationException("Cannot cancel past visit");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found"));
        if(appointment.getStatus()!=AppointmentStatus.RESERVED){
            throw new InvalidAppointmentStatusException("Only a scheduled appointment can be completed");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentEnd = appointment.getEndDateTime();

        if(appointmentEnd.isAfter(now)){
            throw new InvalidAppointmentStatusException("It is not possible to mark an appointment as completed before it is completed");
        }
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }
}
