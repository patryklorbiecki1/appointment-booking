package com.example.appointment_booking.application;

import com.example.appointment_booking.domain.exception.ForbiddenOperationException;
import com.example.appointment_booking.domain.exception.SlotAlreadyTakenException;
import com.example.appointment_booking.domain.exception.SpecialistNotFoundException;
import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.AppointmentStatus;
import com.example.appointment_booking.domain.model.Role;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.UserRepository;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public Appointment bookAppointment(AppointmentRequest request, User patient){
        User specialist = userRepository.findById(request.getSpecialistId())
                .orElseThrow(()-> new SpecialistNotFoundException("Specialist not found"));

        if(patient.getRole()!= Role.PATIENT){
            throw new ForbiddenOperationException("Only patients can book appointments");
        }

        boolean isTaken = appointmentRepository.existsBySpecialistAndDateTime(specialist,request.getDateTime());
        if (isTaken) {
            throw new SlotAlreadyTakenException("This appointment slot is already taken");
        }
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDateTime(request.getDateTime());
        appointment.setSpecialist(specialist);
        appointment.setStatus(AppointmentStatus.RESERVED);

        return appointmentRepository.save(appointment);
    }
}
