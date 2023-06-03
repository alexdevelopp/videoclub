package org.ieschabas.videoclub.backend.security;

import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDetailsService(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userService.findByMail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();


        if (usuario == null) {
            throw new UsernameNotFoundException("El usuario no se encontró.");
        }

        String password = usuario.getPassword();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
        return new User(username, password, authorities);
    }


}
