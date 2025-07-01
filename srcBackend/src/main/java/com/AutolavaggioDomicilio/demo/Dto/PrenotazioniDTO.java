package com.AutolavaggioDomicilio.demo.Dto;

import com.AutolavaggioDomicilio.demo.Entity.Prenotazione;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrenotazioniDTO {
    private Long id;
    private Long userId;
    private Long washerId;
    private LocalDateTime appointmentDateTime;
    private Prenotazione.WashType washType;
    private Prenotazione.AppointmentStatus status;

    public static PrenotazioniDTO mapToDTO(Prenotazione prenotazione) {
        PrenotazioniDTO dto = new PrenotazioniDTO();
        dto.setId(prenotazione.getId());
        dto.setUserId(prenotazione.getUser().getId());
        dto.setWasherId(prenotazione.getWasher().getId());
        dto.setAppointmentDateTime(prenotazione.getAppointmentDateTime());
        dto.setWashType(prenotazione.getWashType());
        dto.setStatus(prenotazione.getStatus());
        return dto;
    }
}