package com.example.appointment_booking.domain.exception;

public class PastAppointmentCancellationException extends RuntimeException{
    public PastAppointmentCancellationException(String message){
        super(message);
    }
}
