package com.example.appointment_booking;

import com.example.appointment_booking.application.VisitNoteService;
import com.example.appointment_booking.config.security.JwtUtil;
import com.example.appointment_booking.domain.exception.AppointmentNotFoundException;
import com.example.appointment_booking.domain.model.*;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.UserRepository;
import com.example.appointment_booking.domain.repository.VisitNoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitNoteServiceTest {
    @Mock
    private VisitNoteRepository noteRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private VisitNoteService visitNoteService;

    @Test
    void createNote_shouldThrow_whenAppointmentNotFound(){

        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AppointmentNotFoundException.class,
                ()-> visitNoteService.createNote(1L,"content"));
    }

    @Test
    void createNote_shouldThrow_whenUserIsNotSpecialist(){
        Long appointmentId = 1L;
        User nonSpecialistUser = new User();
        nonSpecialistUser.setRole(Role.PATIENT);
        nonSpecialistUser.setId(100L);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(nonSpecialistUser));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(new Appointment()));

        assertThrows(AccessDeniedException.class,()->
                visitNoteService.createNote(appointmentId,"content"));
    }

    @Test
    void createNote_shouldCreateNote_whenAllConditionsAreMet(){

        String content = "Text";
        User specialistUser = new User();
        specialistUser.setUsername("specialist");
        specialistUser.setRole(Role.SPECIALIST);
        specialistUser.setId(100L);

        Specialist specialist = new Specialist();
        specialist.setUser(specialistUser);
        specialist.setId(20L);

        Appointment appointment = new Appointment();
        Long appointmentId = 1L;
        appointment.setId(appointmentId);
        appointment.setSpecialist(specialist);
        appointment.setVisitNote(null);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                specialistUser.getUsername(), "password", List.of()
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername("specialist")).thenReturn(Optional.of(specialistUser));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(noteRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VisitNote result = visitNoteService.createNote(appointmentId,content);

        assertNotNull(result);
        assertEquals(content,result.getContent());
        assertEquals(appointment,result.getAppointment());
        verify(noteRepository,times(1)).save(any(VisitNote.class));
        SecurityContextHolder.clearContext();

    }

    @Test
    void getNoteByAppointmentId_shouldGetNote_whenAllConditionsAreMet(){

        Long appointmentId = 1L;
        User specialistUser = new User();
        specialistUser.setUsername("specialist");
        specialistUser.setRole(Role.SPECIALIST);
        specialistUser.setId(100L);

        User patient = new User();
        specialistUser.setRole(Role.PATIENT);
        specialistUser.setId(50L);

        Specialist specialist = new Specialist();
        specialist.setUser(specialistUser);
        specialist.setId(20L);

        VisitNote visitNote = new VisitNote();
        visitNote.setContent("text");
        visitNote.setId(1L);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setSpecialist(specialist);
        appointment.setVisitNote(visitNote);
        appointment.setPatient(patient);

        mockStaticJwtUtil("specialist");
        when(userRepository.findByUsername("specialist")).thenReturn(Optional.of(specialistUser));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        VisitNote result = visitNoteService.getNoteByAppointmentId(appointmentId);

        assertNotNull(result);
        assertEquals(visitNote.getId(),result.getId());
        assertEquals("text",result.getContent());
    }

    private void mockStaticJwtUtil(String username){
        MockedStatic<JwtUtil> jwtUtilMockedStatic = mockStatic(JwtUtil.class);
        jwtUtilMockedStatic.when(JwtUtil::getCurrentUsername).thenReturn(username);
    }


}
