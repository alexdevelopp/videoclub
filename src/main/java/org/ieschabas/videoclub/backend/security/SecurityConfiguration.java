package org.ieschabas.videoclub.backend.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.ieschabas.videoclub.frontend.userviews.LoginView;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)

public class SecurityConfiguration extends VaadinWebSecurity {

    public SecurityConfiguration(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    private final UsuarioDetailsService usuarioDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(usuarioDetailsService);
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }


}