package org.ieschabas.videoclub.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Component
@Getter
@Setter
@NoArgsConstructor
public class AlquilerPK implements Serializable {
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Usuario cliente;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Pelicula pelicula;
    private LocalDate fechaDevolucion;

    public AlquilerPK(Usuario cliente,Pelicula pelicula,LocalDate fechaDevolucion){
        this.cliente = cliente;
        this.pelicula = pelicula;
        this.fechaDevolucion = fechaDevolucion;
    }


}
