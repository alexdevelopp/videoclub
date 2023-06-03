package org.ieschabas.videoclub.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ieschabas.videoclub.backend.enums.Categoria;
import org.ieschabas.videoclub.backend.enums.Valoracion;
import org.ieschabas.videoclub.backend.enums.Formato;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Set;

@Component
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Pelicula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idPelicula;
    private String titulo;
    private Integer anyoPublicacion;
    private String duracion;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Enumerated(EnumType.STRING)
    private Formato formato;
    @Enumerated(EnumType.STRING)
    private Valoracion valoracion;
    private String descripcion;

    private String portada;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "pelicula_actor",
            joinColumns = @JoinColumn(name = "pelicula_id",referencedColumnName = "idPelicula"),
            inverseJoinColumns = @JoinColumn(name = "actor_id",referencedColumnName = "idEquipo"))
    private Set<Equipo> actores;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "pelicula_director",
            joinColumns = @JoinColumn(name = "pelicula_id",referencedColumnName = "idPelicula"),
            inverseJoinColumns = @JoinColumn(name = "director_id",referencedColumnName = "idEquipo"))
    private Set<Equipo>directores;





    public Pelicula(String titulo, int anyoPublicacion, String duracion, Categoria categoria, Formato formato, Valoracion valoracion, String descripcion) {
        this.titulo = titulo;
        this.anyoPublicacion = anyoPublicacion;
        this.duracion = duracion;
        this.categoria = categoria;
        this.formato = formato;
        this.valoracion = valoracion;
        this.descripcion = descripcion;
    }


}
