package com.AutolavaggioDomicilio.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
//@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @Column(nullable = true, unique = true)
    private String vehicleType;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false)
    private String role; // admin, user, washer, superUser

    @Getter
    @Version
    private Long version; // Per locking ottimistico

}