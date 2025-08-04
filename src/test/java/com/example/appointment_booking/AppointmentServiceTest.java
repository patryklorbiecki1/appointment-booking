package com.example.appointment_booking;

import com.example.appointment_booking.application.AppointmentService;
import com.example.appointment_booking.domain.exception.*;
import com.example.appointment_booking.domain.model.*;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.SpecialistRepository;
import com.example.appointment_booking.web.dto.AppointmentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void bookAppointment_shouldBookAppointment_whenAllConditionsAreMet(){

        User patient = createPatient();
        Specialist specialist = createSpecialist();

        LocalDateTime dateTime = LocalDateTime.of(2025,8,10,10,0);
        AppointmentRequest request = createAppointmentRequest(specialist.getId(),dateTime,30);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(10L);

        when(specialistRepository.findByUserId(1L)).thenReturn(Optional.of(specialist));
        when(appointmentRepository.hasConflict(eq(specialist),eq(dateTime),eq(dateTime.plusMinutes(30))))
                .thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

        Appointment result = appointmentService.bookAppointment(request,patient);

        assertNotNull(result);
        assertEquals(10L,result.getId());
        verify(appointmentRepository).save(any(Appointment.class));

    }

    @Test
    void bookAppointment_shouldThrow_whenSpecialistNotFound(){
        LocalDateTime dateTime = LocalDateTime.of(2025,8,10,10,0);
        AppointmentRequest request = createAppointmentRequest(99L,dateTime,60);

        User patient = createPatient();

        when(specialistRepository.findByUserId(99L)).thenReturn(Optional.empty());

        assertThrows(SpecialistNotFoundException.class, () ->
            appointmentService.bookAppointment(request,patient));
    }

    @Test
    void bookAppointment_shouldThrow_whenUserIsNotPatient(){
        LocalDateTime dateTime = LocalDateTime.of(2025,8,10,10,0);
        AppointmentRequest request = createAppointmentRequest(99L,dateTime,30);

        User user = new User();
        user.setRole(Role.ADMIN);

        when(specialistRepository.findByUserId(99L)).thenReturn(Optional.of(new Specialist()));

        assertThrows(ForbiddenOperationException.class, () ->
                appointmentService.bookAppointment(request,user));
    }

    @Test
    void bookAppointment_shouldThrow_whenSlotIsTaken(){
        LocalDateTime dateTime = LocalDateTime.of(2025,8,10,10,0);
        AppointmentRequest request = createAppointmentRequest(1L,dateTime,30);

        User patient = createPatient();

        Specialist specialist = createSpecialist();

        when(specialistRepository.findByUserId(1L)).thenReturn(Optional.of(specialist));
        when(appointmentRepository.hasConflict(eq(specialist),eq(dateTime),eq(dateTime.plusMinutes(30))))
                .thenReturn(true);

        assertThrows(SlotAlreadyTakenException.class, ()->
                appointmentService.bookAppointment(request,patient));
    }

    @Test
    void cancelAppointment_shouldThrow_whenNotFound(){

        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, ()->
                appointmentService.cancelAppointment(1L));
    }

    @Test
    void cancelAppointment_shouldThrow_whenAppointmentIsNotReserved(){
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(RuntimeException.class, ()->
               appointmentService.cancelAppointment(1L));
    }

    @Test
    void cancelAppointment_shouldThrowException_whenAppointmentIsInThePast(){
        Appointment appointment = new Appointment();
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = startTime.plusMinutes(30);

        appointment.setStartDateTime(startTime);
        appointment.setEndDateTime(endTime);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        assertThrows(RuntimeException.class,()->
                appointmentService.cancelAppointment(1L));
    }

    @Test
    void cancelAppointment_shouldCancelAppointment_whenAllConditionsAreMet(){

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusMinutes(60);
        Appointment appointment = new Appointment();
        appointment.setStartDateTime(startTime);
        appointment.setEndDateTime(endTime);
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.RESERVED);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(1L);
        assertEquals(AppointmentStatus.CANCELLED,appointment.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void completeAppointment_shouldThrow_whenIsNotReserved(){
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        assertThrows(InvalidAppointmentStatusException.class, ()->
                appointmentService.completeAppointment(1L));
    }

    @Test
    void completeAppointment_shouldThrow_whenAppointmentIsInFuture(){
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.RESERVED);
        appointment.setEndDateTime(LocalDateTime.now().plusMinutes(30));

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        assertThrows(InvalidAppointmentStatusException.class,()->
                appointmentService.completeAppointment(1L));
    }
    private User createPatient(){
        User patient = new User();
        patient.setId(2L);
        patient.setRole(Role.PATIENT);
        patient.setUsername("adam");
        return patient;
    }
    private User createSpecialistUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("drZieba");
        user.setRole(Role.SPECIALIST);
        return user;
    }
    private Specialist createSpecialist(){
        Specialist specialist = new Specialist();
        specialist.setId(1L);
        specialist.setUser(createSpecialistUser());
        specialist.setSpecialization("Cardiology");
        return specialist;
    }
    private AppointmentRequest createAppointmentRequest(Long specialistId,
                                                        LocalDateTime start,
                                                        int duration){
        AppointmentRequest req = new AppointmentRequest();
        req.setSpecialistId(specialistId);
        req.setDateTime(start);
        req.setDuration(duration);
        return req;
    }
}
