package com.example.appointment_booking.web.controller;

import com.example.appointment_booking.application.VisitNoteService;
import com.example.appointment_booking.domain.model.VisitNote;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class VisitNoteController {

    private final VisitNoteService noteService;

    @PostMapping("/{appointmentId}")
    public VisitNote addNote(@PathVariable Long appointmentId,
                                             @RequestBody Map<String,String> request){
        String content = request.get("content");
        return noteService.createNote(appointmentId,content);
    }
    @GetMapping("/{appointmentId}")
    public VisitNote getNote(@PathVariable Long appointmentId){
        return noteService.getNoteByAppointmentId(appointmentId);
    }
}
