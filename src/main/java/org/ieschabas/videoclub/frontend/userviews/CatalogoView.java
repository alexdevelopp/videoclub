package org.ieschabas.videoclub.frontend.userviews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Alquiler;
import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.ieschabas.videoclub.backend.service.AlquilerService;
import org.ieschabas.videoclub.backend.service.PeliculaService;
import org.ieschabas.videoclub.backend.service.UserService;
import org.ieschabas.videoclub.frontend.styles.Components;
import org.ieschabas.videoclub.frontend.styles.Header;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Route("catalogo")
@PageTitle("Catalogo de peliculas")
@RolesAllowed({"cliente", "administrador"})
public class CatalogoView extends VerticalLayout {

    private SecurityService securityService;
    private H2 title;

    private Usuario usuario;

    private PeliculaService peliculaService;

    private AlquilerService alquilerService;

    private UserService userService;

    private UserDetails userDetails;

    private List<Pelicula> listaPeliculas;

    private Button btnAlquilar;

    private Components component;

    private RouterLink fichaPeliculaLink;


    public CatalogoView(PeliculaService peliculaService, AlquilerService alquilerService, UserService userService, SecurityService securityService) {
        super();
        this.securityService = securityService;
        this.peliculaService = peliculaService;
        this.alquilerService = alquilerService;
        this.userService = userService;
        userDetails = securityService.getAuthenticatedUser();
        usuario = userService.findByMail(userDetails.getUsername());
        component = new Components();
        title = new H2("Catalogo de peliculas");
        listaPeliculas = peliculaService.getAll();
        Header header = new Header(securityService, userService);
        setSizeFull();
        getElement().getStyle().set("padding", "0px");
        add(header, title, createGrid());
    }

    private VerticalLayout fichaPelicula(Pelicula pelicula) {
        btnAlquilar = new Button("Alquilar");
        btnAlquilar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAlquilar.getStyle().set("width", "50%").set("margin", "auto");
        if (pelicula == null) return new VerticalLayout();

        //Este metodo es para saber si una pelicula esta alquilada por el usuario para que no pueda volverla a alquilar
        Optional<Alquiler> peliculaNoDisponible = alquilerService.disponibleOrNot(pelicula.getIdPelicula(), usuario.getIdUsuario());
        if (peliculaNoDisponible.isPresent()) {
            btnAlquilar.setEnabled(false);
            btnAlquilar.setText("No disponible");
        }

        fichaPeliculaLink = new RouterLink("Ficha de la pelicula", FichaPeliculaView.class, pelicula.getTitulo());
        fichaPeliculaLink.addClassName("ver-ficha");
        VerticalLayout layout = new VerticalLayout();
        try {
            Image portada = component.cargarPortada(pelicula);
            portada.addClassName("portada");
            layout.add(portada);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
        layout.add(component.peliculaBox("Titulo", pelicula.getTitulo()));
        layout.add(component.peliculaBox("Valoracion:", component.getStars(pelicula.getValoracion().toString())));
        Div fichaContainer = new Div();
        fichaContainer.addClassName("campos-pelicula");
        fichaContainer.add(fichaPeliculaLink);
        layout.add(fichaContainer);


        //evento que registra alquiler
        btnAlquilar.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            Button cancel = new Button("Cancelar");
            cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
            dialog.setCancelButton(cancel);
            dialog.setCancelable(true);
            dialog.setText("Estas seguro que deseas alquilar la pelicula?");
            dialog.setConfirmText("Aceptar");
            dialog.open();
            dialog.addConfirmListener(event -> {
                Alquiler alquiler = new Alquiler(pelicula, usuario, LocalDate.now(), LocalDate.now().plusDays(2));
                try {
                    alquilerService.save(alquiler);
                    var btn = (Button) e.getSource();
                    btn.setEnabled(false);
                    btn.setText("No disponible");
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setText("La pelicula ha sido alquilada correctamente.");
                    notification.setDuration(2500);
                    notification.open();
                    dialog.close();
                } catch (Exception exception) {
                    Notification.show(exception.getMessage());
                }
            });


        });
        layout.add(btnAlquilar);
        layout.setAlignItems(Alignment.START);
        return layout;
    }

    private HorizontalLayout createGrid() {
        HorizontalLayout container = new HorizontalLayout();
        for (Pelicula pelicula : listaPeliculas) {
            container.add(fichaPelicula(pelicula));
        }
        container.addClassName("grid-container");
        container.setSizeFull();
        return container;
    }


}
