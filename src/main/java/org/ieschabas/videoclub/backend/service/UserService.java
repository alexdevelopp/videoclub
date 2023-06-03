package org.ieschabas.videoclub.backend.service;

import lombok.extern.log4j.Log4j;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
        super();
    }

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public void save(Usuario usuario) {
        try {
            userRepository.save(usuario);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Usuario findByEmailAndPassword(String email, String password) {
        try {
            return userRepository.findByEmailAndPassword(email, password);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public Optional<Usuario> getById(Integer id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Usuario> getAll() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    public void delete(Integer id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Usuario findByMail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
