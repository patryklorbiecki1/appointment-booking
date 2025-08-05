package com.example.appointment_booking;

import com.example.appointment_booking.application.AuthService;
import com.example.appointment_booking.config.security.JwtUtil;
import com.example.appointment_booking.domain.exception.UsernameAlreadyExistsException;
import com.example.appointment_booking.domain.model.Role;
import com.example.appointment_booking.domain.model.Specialist;
import com.example.appointment_booking.domain.model.User;
import com.example.appointment_booking.domain.repository.SpecialistRepository;
import com.example.appointment_booking.domain.repository.UserRepository;
import com.example.appointment_booking.web.dto.AuthResponse;
import com.example.appointment_booking.web.dto.LoginRequest;
import com.example.appointment_booking.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldThrow_whenUsernameExists(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("adam");
        request.setPassword("secret");
        request.setRole(Role.PATIENT);

        when(userRepository.findByUsername("adam")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class,()->
                authService.register(request));
    }

    @Test
    void register_shouldRegister_whenUserNameIsUnique(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jan");
        request.setPassword("secret");
        request.setRole(Role.PATIENT);

        when(userRepository.findByUsername("jan")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        String result = authService.register(request);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("jan",savedUser.getUsername());
        assertEquals("encoded",savedUser.getPassword());
        assertEquals(Role.PATIENT,savedUser.getRole());
        assertEquals("User registered",result);
    }

    @Test
    void register_shouldRegisterSpecialist_whenRoleIsSpecialist(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jan");
        request.setPassword("secret");
        request.setRole(Role.SPECIALIST);

        when(userRepository.findByUsername("jan")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Specialist> specialistCaptor = ArgumentCaptor.forClass(Specialist.class);
        String result = authService.register(request);

        verify(userRepository).save(userCaptor.capture());
        verify(specialistRepository).save(specialistCaptor.capture());
        User savedUser = userCaptor.getValue();
        Specialist savedSpecialist = specialistCaptor.getValue();

        assertEquals("jan",savedUser.getUsername());
        assertEquals("encoded",savedUser.getPassword());
        assertEquals(Role.SPECIALIST,savedUser.getRole());
        assertEquals(savedUser,savedSpecialist.getUser());
        assertEquals("",savedSpecialist.getSpecialization());
        assertEquals("User registered",result);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect(){
        LoginRequest request = new LoginRequest();
        request.setUsername("adam");
        request.setPassword("secret");
        User user = new User(1L,"adam","hashed",Role.PATIENT);

        when(userRepository.findByUsername("adam")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret","hashed")).thenReturn(true);
        when(jwtUtil.generateToken("adam")).thenReturn("mocked_jwt_token");

        ResponseEntity<AuthResponse> response = authService.login(request);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mocked_jwt_token",response.getBody().getToken());
    }

    @Test
    void login_shouldReturn401_whenUserNotFound(){
        LoginRequest request = new LoginRequest();
        request.setUsername("adam");
        request.setPassword("secret");

        when(userRepository.findByUsername("adam")).thenReturn(Optional.empty());

        ResponseEntity<AuthResponse> response = authService.login(request);
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
        assertNull(response.getBody());
    }

}
