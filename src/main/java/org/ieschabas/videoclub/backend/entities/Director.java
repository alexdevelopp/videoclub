package org.ieschabas.videoclub.backend.entities;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

@Entity
@Component
@NoArgsConstructor
@DiscriminatorValue(value="director")

public class Director extends Equipo implements Serializable {



    public Director(String nombre, String apellidos, Integer fecha_nacimiento, String pais,String rol) {
        super(nombre, apellidos, fecha_nacimiento, pais,rol);
    }

}
