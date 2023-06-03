package org.ieschabas.videoclub.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorColumn(name="rol")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Equipo  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idEquipo;
    private Integer fecha_nacimiento;

    private String nombre;

    private String apellidos;

    private String pais;
    @Column(name = "rol",insertable = false, updatable = false)
    private String rol;

    @ManyToMany(mappedBy = "actores",fetch = FetchType.LAZY)
    private List<Pelicula> peliculasActores;

    @ManyToMany(mappedBy = "directores",fetch = FetchType.LAZY)
    private List<Pelicula> peliculasDirectores;



    public Equipo(String nombre, String apellidos, Integer fecha_nacimiento, String pais,String rol) {
        this.fecha_nacimiento = fecha_nacimiento;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.pais = pais;
        this.rol = rol;
    }



}
