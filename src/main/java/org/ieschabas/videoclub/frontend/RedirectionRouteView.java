package org.ieschabas.videoclub.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.service.UserService;
import org.ieschabas.videoclub.frontend.adminviews.PeliculasView;
import org.ieschabas.videoclub.frontend.userviews.CatalogoView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Route("")
@RolesAllowed({"cliente", "administrador"})
public class RedirectionRouteView extends VerticalLayout implements AfterNavigationObserver {

    private Usuario user;

    private final UserService userService;

    public RedirectionRouteView(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        Authentication usuario = SecurityContextHolder.getContext().getAuthentication();
        user = userService.findByMail(usuario.getName());
        if (user.getRol().equalsIgnoreCase("cliente")) {
            UI.getCurrent().navigate(CatalogoView.class);
        } else {
            UI.getCurrent().navigate(PeliculasView.class);
        }
    }

}
