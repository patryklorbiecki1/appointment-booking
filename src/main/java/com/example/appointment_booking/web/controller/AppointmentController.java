package com.example.appointment_booking.web.controller;


import com.example.appointment_booking.application.AppointmentService;
import com.example.appointment_booking.application.UserService;
import com.example.appointment_booking.domain.model.Appointment;
import com.example.appointment_booking.domain.model.Role;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getCurrentUser(userDetails);
        if(user.getRole()!= Role.PATIENT){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only patients can book appointments");
        }
        Appointment booked = appointmentService.bookAppointment(request,user);
        return ResponseEntity.ok(booked);
    }

}
