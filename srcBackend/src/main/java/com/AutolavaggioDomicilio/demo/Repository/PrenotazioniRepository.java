package com.AutolavaggioDomicilio.demo.Repository;

import com.AutolavaggioDomicilio.demo.Entity.Prenotazione;
import com.AutolavaggioDomicilio.demo.Entity.User;
import com.AutolavaggioDomicilio.demo.Entity.Washer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByWasher(Washer washer);

    List<Prenotazione> findByUser(User user);
}