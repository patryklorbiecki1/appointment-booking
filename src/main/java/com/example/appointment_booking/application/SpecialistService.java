package com.example.appointment_booking.application;

import com.example.appointment_booking.config.security.JwtUtil;
import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.UserRepository;
import com.example.appointment_booking.web.dto.AppointmentDto;
import com.example.appointment_booking.web.dto.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;

    public List<AppointmentDto> getUpcomingAppointmentsForSpecialist(){
        String username = JwtUtil.getCurrentUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long specialistId = currentUser.getId();
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsForSpecialist(specialistId);

        return appointmentMapper.doDtoList(appointments);

    }
}
