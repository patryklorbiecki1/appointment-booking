package com.example.appointment_booking.domain.exception;

public class SpecialistNotFoundException extends RuntimeException{
    public SpecialistNotFoundException(String message){
        super(message);
    }
}
