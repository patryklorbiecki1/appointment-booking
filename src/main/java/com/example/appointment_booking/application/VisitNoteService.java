package com.example.appointment_booking.application;

import com.example.appointment_booking.config.security.JwtUtil;
import com.example.appointment_booking.domain.exception.AppointmentAlreadyExistsException;
import com.example.appointment_booking.domain.exception.AppointmentNotFoundException;
import com.example.appointment_booking.domain.model.*;
import com.example.appointment_booking.domain.repository.AppointmentRepository;
import com.example.appointment_booking.domain.repository.UserRepository;
import com.example.appointment_booking.domain.repository.VisitNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VisitNoteService {

    private final VisitNoteRepository noteRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public VisitNote createNote(Long appointmentId,String content){
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found"));

        User currentUser = currentUser();
        if(!userIsSpecialist(currentUser)){
            throw new AccessDeniedException("Only specialists can create visit notes");
        }

        if(!Objects.equals(appointment.getSpecialist().getUser().getId(),currentUser.getId())){
            throw new AccessDeniedException("You are not the assigned specialist for this appointment");
        }

        if(appointment.getVisitNote() !=null){
            throw new AppointmentAlreadyExistsException("Note already exists for this appointment");
        }
        VisitNote visitNote = new VisitNote();
        visitNote.setAppointment(appointment);
        visitNote.setContent(content);
        visitNote.setCreatedAt(LocalDateTime.now());

        return noteRepository.save(visitNote);
    }

    private User currentUser(){
        String currentUsername = JwtUtil.getCurrentUsername();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    private boolean userIsSpecialist(User user){
        return user.getRole() == Role.SPECIALIST;
    }

    public VisitNote getNoteByAppointmentId(Long appointmentId){
        User currentUser = userRepository.findByUsername(JwtUtil.getCurrentUsername())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(()->new AppointmentNotFoundException("Appointment not found"));
        if(!isUserAuthorizedForAppointment(currentUser,appointment)){
            throw new AccessDeniedException("You do not have access to this note");
        }
        VisitNote visitNote = appointment.getVisitNote();
        if(visitNote == null){
            throw new AccessDeniedException("Visit note not found");
        }
        return visitNote;
    }

    private boolean isUserAuthorizedForAppointment(User user, Appointment appointment) {
        Long userId = user.getId();
        return userId.equals(appointment.getPatient().getId()) ||
                userId.equals(appointment.getSpecialist().getId());
    }
}
