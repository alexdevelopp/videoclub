package org.ieschabas.videoclub.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorColumn(name = "rol")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "usuarios")
@Component
public abstract class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer idUsuario;

    protected String nombre;

    protected String apellidos;
    protected String direccion;
    protected Boolean activo;
    protected LocalDate fechaRegistro;

    protected String email;

    protected String password;

    @Column(name = "rol",insertable = false, updatable = false)
    protected String rol;


    public Usuario(String nombre, String apellidos, String direccion, LocalDate fechaRegistro, String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.email = email;
        this.password = password;
        this.activo = true;
    }


}
