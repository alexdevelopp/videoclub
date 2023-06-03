package org.ieschabas.videoclub.backend.entities;


import org.springframework.stereotype.Component;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;


@Entity
@Component
@DiscriminatorValue(value = "cliente")

public class Cliente extends Usuario {

    public Cliente(){super();}

    public Cliente(String nombre, String apellidos, String direccion, LocalDate fechaRegistro, String email, String password) {
        super(nombre, apellidos, direccion, fechaRegistro,email,password);
    }

}
