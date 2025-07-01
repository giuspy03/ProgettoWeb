package com.AutolavaggioDomicilio.demo.Controller;

import com.AutolavaggioDomicilio.demo.Dto.PrenotazioneRequest;
import com.AutolavaggioDomicilio.demo.Dto.PrenotazioniDTO;
import com.AutolavaggioDomicilio.demo.Dto.WasherAvailabilityRequest;
import com.AutolavaggioDomicilio.demo.Entity.Prenotazione;
import com.AutolavaggioDomicilio.demo.Entity.User;
import com.AutolavaggioDomicilio.demo.Service.PrenotazioniService;
import com.AutolavaggioDomicilio.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final PrenotazioniService prenotazioniService;
    private final UserService userService;


    //creo appuntamento
    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(
            @RequestBody PrenotazioneRequest request) {
        // Aggiungi controllo esplicitoAGGIUNTODOPOOOOO
        if (request.getWasherId() == null) {
            throw new IllegalArgumentException("ID Washer mancante");
        }

        try {
            PrenotazioniDTO appointment = prenotazioniService.createAppointment(request);
            return ResponseEntity.created(URI.create("/appointments/" + appointment.getId()))
                    .body(appointment);
        } catch (SlotNotAvailableException ex) {

        }
        return null;
    }

    //promuovo a washer passando username (non id)
    @PutMapping("/promote/washer")
    public ResponseEntity<?> promoteWasher(@RequestBody String username) {
        User userEntity = userService.loadUserByUsername(username); // <-- Non crea
        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if ("WASHER".equals(userEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already washer");
        }
        userService.promoteWasher(userEntity);
        return ResponseEntity.ok().build();
    }

    //promuovo admin passando username (non id)
    @PutMapping("/promote/admin")
    public ResponseEntity<?> promoteAdmin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        User userEntity = userService.loadUserByUsername(username);

        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }
        if ("ADMIN".equals(userEntity.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utente è già admin");
        }

        userService.promoteAdmin(userEntity);
        return ResponseEntity.ok().build();
    }

    /*
    Passo username (non id) del washer da rimuovere e lo rimuovo,
    */
    @PutMapping("/revoke/washer")
    public ResponseEntity<?> revokeWasher(@RequestBody String user) {
        User userEntity = userService.loadUserByUsername(user);
        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!userEntity.getRole().equals("WASHER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        userService.revokeRole(userEntity);
        return ResponseEntity.ok().build();
    }

    /*
    Passo username (non id) dell'admin da rimuovere e lo rimuovo,
    sarebbe interessante un check per non autorimuoversi, potrebbero non rimanere admin anche se esiste il superuser
     */
    @PutMapping("/revoke/admin")
    public ResponseEntity<?> revokeAdmin(@RequestBody String user) {
        User userEntity = userService.loadUserByUsername(user);
        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!userEntity.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        userService.revokeRole(userEntity);
        return ResponseEntity.ok().build();
    }

    /*
    un washer inserisce un intervallo di disponibilità, la request comprende:
    Long washerId, LocalDateTime availableFrom, LocalDateTime availableTo.
    La disponibilità viene inserita per uno slot di tempo indeterminato, ogni wash dura però un'ora
     */
    @PostMapping("/availability/washer")
    public ResponseEntity<?> addAvailability(@RequestBody WasherAvailabilityRequest request) {
        return ResponseEntity.ok(prenotazioniService.addWasherAvailability(request));
    }



    /*
    Fornisce la lista degli appuntamenti di un washer, la requestparam comprende l'id del washer
     */
    @GetMapping("/appointmentsToDo/washer")
    public ResponseEntity<?> getWasherAppointments(@RequestParam String username) {
        User washer = userService.loadUserByUsername(username);
        if (washer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }
        return ResponseEntity.ok(prenotazioniService.getAppointmentsByWasher(washer.getId()));
    }

    /*
    Fornisce la lista delle prenotazioni, la requestparam comprende l'id dell'utente
     */
    @GetMapping("/appointments/washer")
    public ResponseEntity<List<Prenotazione>> getUserAppointments(
            @RequestParam Long userId) {
        return ResponseEntity.ok(prenotazioniService.getAppointmentsByUser(userId));
    }


    /*
    Per verificare che postman funzioni e che il superuser sia valido con bearer token (vedi POST su Postman)
     */
    @GetMapping("/testBackend")
    public ResponseEntity<?> testBackend() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserId(JwtAuthenticationToken authentication) {
        String username = authentication.getName(); // recupera lo username dal token
        User user = userService.loadUserByUsername(username);
        return ResponseEntity.ok(Map.of("userId", user.getId()));
    }
}
