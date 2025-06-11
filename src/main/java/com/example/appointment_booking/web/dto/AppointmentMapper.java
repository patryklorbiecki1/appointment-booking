package com.example.appointment_booking.web.dto;

import com.example.appointment_booking.domain.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment){
        AppointmentDto dto = new AppointmentDto();
        dto.setStartDateTime(appointment.getStartDateTime());
        dto.setSpecialistId(appointment.getId());
        dto.setEndDateTime(appointment.getEndDateTime());
        return dto;
    }
}
