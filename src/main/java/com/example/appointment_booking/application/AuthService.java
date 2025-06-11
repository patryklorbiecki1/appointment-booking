package com.example.appointment_booking.application;

import com.example.appointment_booking.config.security.JwtUtil;
import com.example.appointment_booking.domain.exception.UsernameAlreadyExistsException;
import com.example.appointment_booking.domain.model.Role;
import com.example.appointment_booking.domain.model.Specialist;
import com.example.appointment_booking.web.dto.AuthResponse;
import com.example.appointment_booking.web.dto.LoginRequest;
import com.example.appointment_booking.web.dto.RegisterRequest;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request){
        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        if(Role.SPECIALIST.equals(request.getRole())){
            Specialist specialist = new Specialist();
            specialist.setSpecialization("");
            specialist.setUser(user);
            user.setSpecialist(specialist);
        }
        userRepository.save(user);
        return "User registered";
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request){
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
