package org.ieschabas.videoclub.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.ieschabas.videoclub.frontend.styles.Header;
import org.ieschabas.videoclub.frontend.adminviews.AlquileresView;
import org.ieschabas.videoclub.frontend.adminviews.EquipoView;
import org.ieschabas.videoclub.frontend.adminviews.PeliculasView;
import org.ieschabas.videoclub.frontend.userviews.CatalogoView;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.ieschabas.videoclub.backend.service.UserService;
import org.ieschabas.videoclub.frontend.styles.Components;


public class MainView extends AppLayout {
    private final String ACTOR = "actor";
    private final String DIRECTOR = "director";

    private final RouterLink directoresLink = new RouterLink("Directores", EquipoView.class, DIRECTOR);

    private final RouterLink actoresLink = new RouterLink("Actores", EquipoView.class, ACTOR);

    private final RouterLink alquileresLink = new RouterLink("Alquileres", AlquileresView.class);

    private final RouterLink peliculasLink = new RouterLink("Peliculas", PeliculasView.class);

    private final RouterLink catalogoLink = new RouterLink("Catalogo", CatalogoView.class);

    private Tabs enlaces;

    private UserService userService;
    private SecurityService securityService;


    public MainView(SecurityService securityService, UserService userService) {
        super();
        this.securityService = securityService;
        this.userService = userService;
        Components navBar = new Components();
        enlaces = navBar.getTabsMenu(actoresLink, directoresLink, alquileresLink, peliculasLink, catalogoLink);
        Header header = new Header(securityService, userService);
        addToDrawer(enlaces);
        addToNavbar(header);
    }

}
