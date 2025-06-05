package com.example.appointment_booking.web.dto;

import com.example.appointment_booking.domain.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
