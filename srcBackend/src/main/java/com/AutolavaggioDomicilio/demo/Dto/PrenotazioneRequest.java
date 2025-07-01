package com.AutolavaggioDomicilio.demo.Dto;

import com.AutolavaggioDomicilio.demo.Entity.Prenotazione;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Setter
@Getter
public class PrenotazioneRequest {

    private Long userId;
    private Long washerId;
    @Future
    private LocalDateTime appointmentDateTime;
    private Prenotazione.WashType washType;

}
