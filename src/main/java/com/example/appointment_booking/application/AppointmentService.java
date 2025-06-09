package com.example.appointment_booking.application;

import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.AppointmentStatus;
import com.example.appointment_booking.domain.model.Specialist;
import com.example.appointment_booking.domain.model.User;
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
        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(()->new RuntimeException("Specialist not found"));

        boolean isTaken = appointmentRepository.existsBySpecialistAndDateTime(specialist,request.getDateTime());
        if (isTaken) {
            throw new RuntimeException("Appointment slot already taken");
        }
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDateTime(request.getDateTime());
        appointment.setSpecialist(specialist);
        appointment.setStatus(AppointmentStatus.RESERVED);

        return appointmentRepository.save(appointment);
    }
}
