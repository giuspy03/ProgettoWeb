package com.AutolavaggioDomicilio.demo.Service;

import com.AutolavaggioDomicilio.demo.Controller.ResourceNotFoundException;
import com.AutolavaggioDomicilio.demo.Controller.SlotNotAvailableException;
import com.AutolavaggioDomicilio.demo.Dto.PrenotazioniDTO;
import com.AutolavaggioDomicilio.demo.Entity.Prenotazione;
import com.AutolavaggioDomicilio.demo.Entity.User;
import com.AutolavaggioDomicilio.demo.Entity.Washer;
import com.AutolavaggioDomicilio.demo.Dto.PrenotazioneRequest;
import com.AutolavaggioDomicilio.demo.Dto.WasherAvailabilityRequest;
import com.AutolavaggioDomicilio.demo.Entity.WasherAvailability;
import com.AutolavaggioDomicilio.demo.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrenotazioniService {
    private final PrenotazioniRepository appointmentRepository;
    private final WasherAvailabilityRepository availabilityRepository;
    private final WasherRepository washerRepository;
    private final UserRepository userRepository;


    //attenzione, aggiorna la disponibilità del washer rimuovendo l'orario in cui si sta prenotando
    @Transactional
    public PrenotazioniDTO createAppointment(PrenotazioneRequest request) {
        // 1. Verifica esistenza risorse con lock
        Washer washer = washerRepository.findById(request.getWasherId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Washer non trovato con ID: " + request.getWasherId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));


        // 2. Trova e blocca uno slot disponibile
        WasherAvailability availability = availabilityRepository
                .findByWasherAndAvailableFromBetween(
                        washer,
                        request.getAppointmentDateTime(),
                        request.getAppointmentDateTime().plusHours(1)
                ).orElseThrow(() -> new SlotNotAvailableException("Slot non disponibile"));

        // 3. Crea la prenotazione
        Prenotazione appointment = new Prenotazione();
        appointment.setUser(user);
        appointment.setWasher(washer);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setWashType(request.getWashType());
        appointment.setStatus(Prenotazione.AppointmentStatus.SOSPESO);

        // 4. Marca lo slot come prenotato
        availability.setBooked(true);
        availabilityRepository.save(availability);

        Prenotazione savedAppointment = appointmentRepository.save(appointment);

        return mapToDTO(savedAppointment);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void confirmAppointment(Long appointmentId) {
        Prenotazione appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione non trovata"));

        if (appointment.getStatus() != Prenotazione.AppointmentStatus.SOSPESO) {
            throw new IllegalStateException("Prenotazione già confermata o cancellata");
        }

        appointment.setStatus(Prenotazione.AppointmentStatus.CONFERMATO);
        appointmentRepository.save(appointment);
    }

    /*
    Processamento pagamento, per ora inutile e credo per sempre inutile
     */
    @Transactional
    public void processAppointmentPayment(Long appointmentId){

        Prenotazione appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione non trovata"));

        // Verifica che la prenotazione sia in stato SOSPESO
        if (appointment.getStatus() != Prenotazione.AppointmentStatus.SOSPESO) {
            throw new IllegalStateException("Impossibile processare il pagamento: stato non valido");
        }

        // Calcola l'importo in base al tipo di lavaggio
        double amount = switch (appointment.getWashType()) {
            case BASIC -> 10.0;
            case NORMAL -> 15.0;
            case PREMIUM -> 25.0;
        };

        try {


            // Aggiorna lo stato se il pagamento ha successo
            appointment.setStatus(Prenotazione.AppointmentStatus.CONFERMATO);
            appointmentRepository.save(appointment);

        } catch (RuntimeException e) {
            // In caso di errore, mantieni lo stato SOSPESO
            appointment.setStatus(Prenotazione.AppointmentStatus.SOSPESO);
            appointmentRepository.save(appointment);
            throw e;
        }
    }

    // New method to get appointments by washer
    public List<Prenotazione> getAppointmentsByWasher(Long washerId) {
        Washer washer = washerRepository.findById(washerId)
                .orElseThrow(() -> new ResourceNotFoundException("Washer not found"));

        return appointmentRepository.findByWasher(washer);
    }

    public List<Prenotazione> getAppointmentsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return appointmentRepository.findByUser(user);
    }

    /*
    Metodo che depreco in quanto il lavaggio dura di default un'ora, qui imposto la data di fine
    @Transactional
    public WasherAvailability addWasherAvailability(WasherAvailabilityRequest request) {
        Washer washer = washerRepository.findById(request.getWasherId())
                .orElseThrow(() -> new ResourceNotFoundException("Washer not found"));

        // Check if the time slot is valid
        if (request.getAvailableFrom().isAfter(request.getAvailableTo())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Check for overlapping availability
        boolean hasOverlap = availabilityRepository.existsByWasherAndTimeRange(
                washer.getId(),
                request.getAvailableFrom(),
                request.getAvailableTo());

        if (hasOverlap) {
            throw new SlotNotAvailableException("Time slot overlaps with existing availability");
        }

        WasherAvailability availability = new WasherAvailability();
        availability.setWasher(washer);
        availability.setAvailableFrom(request.getAvailableFrom());
        availability.setAvailableTo(request.getAvailableTo());
        availability.setBooked(false);

        return availabilityRepository.save(availability);
    }
    */

    @Transactional
    public WasherAvailability addWasherAvailability(WasherAvailabilityRequest request) {
        Washer washer = washerRepository.findById(request.getWasherId())
                .orElseThrow(() -> new ResourceNotFoundException("Washer not found"));

        // Calcola automaticamente la fine aggiungendo 1 ora all'inizio
        LocalDateTime availableTo = request.getAvailableFrom().plusHours(1);

        // Check for overlapping availability
        boolean hasOverlap = availabilityRepository.existsByWasherAndTimeRange(
                washer.getId(),
                request.getAvailableFrom(),
                availableTo);

        if (hasOverlap) {
            throw new SlotNotAvailableException("Time slot overlaps with existing availability");
        }

        WasherAvailability availability = new WasherAvailability();
        availability.setWasher(washer);
        availability.setAvailableFrom(request.getAvailableFrom());
        availability.setAvailableTo(availableTo);  // Usa la data calcolata
        availability.setBooked(false);

        return availabilityRepository.save(availability);
    }

    private PrenotazioniDTO mapToDTO(Prenotazione prenotazione) {
        // Implementation from AppointmentDTO
        return PrenotazioniDTO.mapToDTO(prenotazione);
    }
}