package org.ieschabas.videoclub.backend.repositories;
import org.ieschabas.videoclub.backend.entities.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler,Integer> {

    @Query("SELECT a FROM Alquiler a " +
            "WHERE a.id.pelicula.id = :idPelicula " +
            "AND a.id.cliente.id = :idCliente " +
            "AND a.id.fechaDevolucion > CURRENT_DATE")
    Optional<Alquiler> findFirstAlquilerPendiente(@Param("idPelicula")int peliculaId, @Param("idCliente") int clienteId);

}
