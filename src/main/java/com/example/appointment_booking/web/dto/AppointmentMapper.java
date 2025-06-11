package com.example.appointment_booking.web.dto;

import com.example.appointment_booking.domain.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment){
        AppointmentDto dto = new AppointmentDto();
        dto.setDateTime(appointment.getDateTime());
        dto.setSpecialistId(appointment.getId());
        return dto;
    }
}
