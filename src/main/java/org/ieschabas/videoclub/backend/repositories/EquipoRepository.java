package org.ieschabas.videoclub.backend.repositories;

import org.ieschabas.videoclub.backend.entities.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
     List<Equipo> findByRol(String rol);

}
