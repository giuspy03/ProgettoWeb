package com.AutolavaggioDomicilio.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "washer_availability")
public class WasherAvailability {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Washer washer;

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDateTime availableFrom;

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDateTime availableTo;

    @Column(nullable = false)
    private boolean isBooked = false;

    @Setter
    @Version
    private Long version;

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

}
