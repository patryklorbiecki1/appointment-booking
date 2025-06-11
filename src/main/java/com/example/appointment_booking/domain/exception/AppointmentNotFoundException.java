package com.example.appointment_booking.domain.exception;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message){
        super(message);
    }
}
