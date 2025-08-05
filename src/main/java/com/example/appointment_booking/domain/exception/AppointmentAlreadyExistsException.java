package com.example.appointment_booking.domain.exception;

public class AppointmentAlreadyExistsException extends RuntimeException{
    public AppointmentAlreadyExistsException(String message){
        super(message);
    }
}
