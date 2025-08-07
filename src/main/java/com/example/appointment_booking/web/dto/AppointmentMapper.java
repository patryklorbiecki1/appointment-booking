package com.example.appointment_booking.web.dto;

import com.example.appointment_booking.domain.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment){
        AppointmentDto dto = new AppointmentDto();
        dto.setStartDateTime(appointment.getStartDateTime());
        dto.setSpecialistId(appointment.getId());
        dto.setEndDateTime(appointment.getEndDateTime());
        dto.setPatientName(appointment.getPatient().getUsername());
        dto.setVisitNoteContent(appointment.getVisitNote().getContent());
        return dto;
    }
    public List<AppointmentDto> doDtoList(List<Appointment> appointments){
        return appointments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
