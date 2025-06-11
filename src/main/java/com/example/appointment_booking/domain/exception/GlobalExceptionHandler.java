package com.example.appointment_booking.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SlotAlreadyTakenException.class)
    public ResponseEntity<?> handleSlotAlreadyTaken(SlotAlreadyTakenException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(SpecialistNotFoundException.class)
    public ResponseEntity<?> handleSpecialistNotFoundException(SpecialistNotFoundException ex){
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<?> handleForbiddenOperationException(ForbiddenOperationException ex){
        return  ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<?> handleAppointmentNotFoundException(AppointmentNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error",ex.getMessage()));
    }
   /* @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unexpected error"));
    }*/
}
