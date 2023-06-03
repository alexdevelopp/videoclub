package org.ieschabas.videoclub.backend.service;

import lombok.extern.log4j.Log4j;
import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.ieschabas.videoclub.backend.repositories.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class PeliculaService {

    @Autowired
    PeliculaRepository peliculaRepository;

    public PeliculaService() {
        super();
    }

    public void save(Pelicula pelicula) {
        try {
            peliculaRepository.save(pelicula);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<Pelicula> getById(Integer id) {
        try {
            return peliculaRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public Pelicula getByTitle(String title) {
        try {
            return peliculaRepository.findByTitulo(title);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public List<Pelicula> getAll() {
        try {
            return peliculaRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void delete(Integer id) {
        try {
            peliculaRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
