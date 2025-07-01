package com.AutolavaggioDomicilio.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
//@Table(name = "washers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Optional (default)
@DiscriminatorColumn(name = "user_type") // Optional (for SINGLE_TABLE)
public class Washer extends User {
    @Id
    @GeneratedValue
    private Long id;


    @Setter
    @Column(nullable = true)
    private String workArea;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


    //CREDO POSSA ESSERE ELIMINATA DOPO LE MODIFICHE A SEGUITO DI KEYCLOACK

}