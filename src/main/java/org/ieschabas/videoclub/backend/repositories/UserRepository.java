package org.ieschabas.videoclub.backend.repositories;


import org.ieschabas.videoclub.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario,Integer> {
    Usuario findByEmailAndPassword(String email, String password);
    Usuario findByEmail(String email);
}


