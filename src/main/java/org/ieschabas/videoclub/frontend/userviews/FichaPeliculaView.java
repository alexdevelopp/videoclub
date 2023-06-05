package org.ieschabas.videoclub.frontend.userviews;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Equipo;
import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.ieschabas.videoclub.frontend.styles.Components;
import org.ieschabas.videoclub.frontend.styles.Header;
import org.ieschabas.videoclub.backend.service.PeliculaService;
import org.ieschabas.videoclub.backend.service.UserService;





@Route(value = "ficha-pelicula")
@PageTitle("Informacion")
@RolesAllowed({"cliente", "administrador"})
public class FichaPeliculaView extends HorizontalLayout implements HasUrlParameter<String> {

    private Components componentes;

    private Pelicula pelicula;
    private PeliculaService peliculaService;
    private Button back;
    private HorizontalLayout buttonContainer;

    private UserService userService;
    private SecurityService securityService;

    public FichaPeliculaView(PeliculaService peliculaService, UserService userService, SecurityService securityService) {
        super();
        this.addClassName("body-ficha-pelicula");
        this.userService = userService;
        this.securityService = securityService;
        this.peliculaService = peliculaService;
        back = new Button("VOLVER");
        back.addClickListener(e -> {
            UI.getCurrent().navigate(CatalogoView.class);
        });
        Header header = new Header(securityService, userService);
        add(header);
        back.addClassName("login-button");
        componentes = new Components();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String titulo) {
        VerticalLayout containerPortada = new VerticalLayout();
        buttonContainer = new HorizontalLayout();
        buttonContainer.addClassName("container-button");
        buttonContainer.add(back);
        containerPortada.setWidth("25%");
        containerPortada.addClassName("container-portada");
        //Recupero la pelicula a traves del parametro para poder cargarla
        Pelicula pelicula = peliculaService.getByTitle(titulo);
        try {
            Image portada = componentes.cargarPortada(pelicula);
            portada.getStyle().set("width", "100%");
            containerPortada.add(portada);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
        add(containerPortada);
        add(fichaPelicula(pelicula), buttonContainer);

    }

    private VerticalLayout fichaPelicula(Pelicula pelicula) {
        if (pelicula == null) return new VerticalLayout();
        VerticalLayout layout = new VerticalLayout();
        layout.add(componentes.peliculaBox("Titulo", pelicula.getTitulo()));
        layout.add(componentes.peliculaBox("Año de publicacion:", pelicula.getAnyoPublicacion().toString()));
        layout.add(componentes.peliculaBox("Duracion:", pelicula.getDuracion()));
        layout.add(componentes.peliculaBox("Categoria:", pelicula.getCategoria().toString()));
        layout.add(componentes.peliculaBox("Formato:", pelicula.getFormato().toString()));
        layout.add(showCollection("Actores: ", pelicula));
        layout.add(showCollection("Directores: ", pelicula));
        layout.add(componentes.peliculaBox("Valoracion:", componentes.getStars(pelicula.getValoracion().toString())));
        layout.addClassName("container-ficha-pelicula");
        layout.setWidth("30%");
        return layout;
    }

    private Div showCollection(String titulo, Pelicula pelicula) {
        Div div = new Div();
        if (titulo.equalsIgnoreCase("directores: ")) {
            div.addClassName("ficha-pelicula");
            Span labelTitle = new Span(titulo);
            labelTitle.getStyle().set("font-weight", "bold").set("width", "100%");
            div.add(labelTitle);
            for (Equipo director : pelicula.getDirectores()) {
                Span title = new Span(director.getNombre() + " " + director.getApellidos());
                title.getStyle().set("width", "100%");
                div.add(title);
            }
        } else {
            div.addClassName("ficha-pelicula");
            Span labelTitle = new Span(titulo);
            labelTitle.getStyle().set("font-weight", "bold").set("width", "100%");
            div.add(labelTitle);
            for (Equipo actor : pelicula.getActores()) {
                Span title = new Span(actor.getNombre() + " " + actor.getApellidos());
                title.getStyle().set("width", "100%");
                div.add(title);
            }
        }
        return div;
    }


}
