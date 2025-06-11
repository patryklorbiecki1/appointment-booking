package com.example.appointment_booking.domain.exception;

public class SlotAlreadyTakenException extends RuntimeException {
    public SlotAlreadyTakenException(String message){
        super(message);
    }
}
