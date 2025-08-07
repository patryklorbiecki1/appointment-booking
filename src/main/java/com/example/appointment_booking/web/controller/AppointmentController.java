package com.example.appointment_booking.web.controller;


import com.example.appointment_booking.application.AppointmentService;
import com.example.appointment_booking.application.SpecialistService;
import com.example.appointment_booking.application.UserService;
import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.web.dto.AppointmentMapper;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import com.example.appointment_booking.web.dto.AppointmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final AppointmentMapper appointmentMapper;
    private final SpecialistService specialistService;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getCurrentUser(userDetails);
        Appointment booked = appointmentService.bookAppointment(request,user);
        AppointmentDto response = appointmentMapper.toDto(booked);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id){
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment was cancelled");
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<String> completeAppointment(@PathVariable Long id){
        appointmentService.completeAppointment(id);
        return ResponseEntity.ok("The appointment has been marked as completed");
    }

    @GetMapping("/specialist/upcoming")
    @PreAuthorize("hasRole('SPECIALIST')")
    public List<AppointmentDto> getUpcomingAppointments(){
        return specialistService.getUpcomingAppointmentsForSpecialist();
    }
}
