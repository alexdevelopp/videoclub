package org.ieschabas.videoclub.backend.entities;


import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;


@Entity
@Component
@NoArgsConstructor
@DiscriminatorValue(value="actor")

public class Actor extends Equipo implements Serializable {



    public Actor(String nombre, String apellidos, Integer anyoNacimiento, String pais, String rol) {
        super(nombre, apellidos, anyoNacimiento, pais,rol);
    }


}
