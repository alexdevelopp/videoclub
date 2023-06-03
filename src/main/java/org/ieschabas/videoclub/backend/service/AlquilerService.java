package org.ieschabas.videoclub.backend.service;

import lombok.extern.log4j.Log4j;
import org.ieschabas.videoclub.backend.entities.Alquiler;
import org.ieschabas.videoclub.backend.repositories.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class AlquilerService {

    @Autowired
    private final AlquilerRepository alquilerRepository;

    public AlquilerService(AlquilerRepository alquilerRepository) {
        super();
        this.alquilerRepository = alquilerRepository;
    }

    public void save(Alquiler alquiler) {
        try {
            alquilerRepository.save(alquiler);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Alquiler> getById(Integer id) {
        try {
            return alquilerRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Alquiler> getAll() {
        try {
            return alquilerRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void delete(Integer id) {
        try {
            alquilerRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Alquiler> disponibleOrNot(Integer idPelicula, Integer idUsuario) {
        try {
            return alquilerRepository.findFirstAlquilerPendiente(idPelicula, idUsuario);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

}
