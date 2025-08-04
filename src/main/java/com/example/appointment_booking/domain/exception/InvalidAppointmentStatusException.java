package com.example.appointment_booking.domain.exception;

public class InvalidAppointmentStatusException extends RuntimeException{
    public InvalidAppointmentStatusException(String message){
        super(message);
    }
}
