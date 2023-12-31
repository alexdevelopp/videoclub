package org.ieschabas.videoclub.backend.repositories;

import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    Pelicula findByTitulo(String title);
}
