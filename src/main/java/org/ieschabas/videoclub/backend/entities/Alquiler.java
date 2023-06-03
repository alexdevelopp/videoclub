package org.ieschabas.videoclub.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Component
public class Alquiler {

    @Id
    private AlquilerPK alquilerPK;

    @Column(name = "id_alquiler")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAlquiler;
    private LocalDate fechaAlquiler;



    public Alquiler(Pelicula pelicula, Usuario cliente, LocalDate fechaAlquiler, LocalDate fechaDevolucion){
        this.alquilerPK = new AlquilerPK(cliente,pelicula,fechaDevolucion);
        this.fechaAlquiler = fechaAlquiler;
    }

}
