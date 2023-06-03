package org.ieschabas.videoclub.backend.service;

import lombok.extern.log4j.Log4j;
import org.ieschabas.videoclub.backend.entities.Equipo;
import org.ieschabas.videoclub.backend.repositories.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public EquipoService() {
    }

    public void save(Equipo equipo) {
        try {
            equipoRepository.save(equipo);
        } catch (Exception e) {

        }
    }

    public Optional<Equipo> getById(Integer id) {
        try {
            return equipoRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Equipo> getAll(String rol) {
        try {
            return equipoRepository.findByRol(rol);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void delete(Integer id) {
        try {
            equipoRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
