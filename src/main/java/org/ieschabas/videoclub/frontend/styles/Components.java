package org.ieschabas.videoclub.frontend.styles;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.ieschabas.videoclub.backend.entities.Pelicula;

public class Components {


    private Tab linkActores;
    private Tab linkDirectores;
    private Tab linkAlquiler;
    private Tab linkPeliculas;
    private Tab linkCatalogo;

    public Components() {
        super();
    }

    //links

    public Tabs getTabsMenu(RouterLink link1, RouterLink link2, RouterLink link3, RouterLink link4, RouterLink link5) {
        Tabs tabs = new Tabs();
        tabs.add(
                linkActores = createTab(VaadinIcon.USER, link1),
                linkDirectores = createTab(VaadinIcon.USER, link2),
                linkAlquiler = createTab(VaadinIcon.FILM, link3),
                linkPeliculas = createTab(VaadinIcon.FILM, link4),
                linkCatalogo = createTab(VaadinIcon.FILM, link5));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedTab(null);
        linkActores.addClassName("links");
        linkDirectores.addClassName("links");
        linkAlquiler.addClassName("links");
        linkPeliculas.addClassName("links");
        linkCatalogo.addClassName("links");
        return tabs;
    }

    public Tab createTab(VaadinIcon viewIcon, RouterLink link) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        link.add(icon);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    //carga de peliculas
    public Image cargarPortada(Pelicula pelicula) {

        String rutaCompleta = pelicula.getPortada();
        String[] rutaFragmentada = rutaCompleta.split("/");
        String nombreArchivo = rutaFragmentada[2];

        // Cargar la imagen utilizando StreamResource
        StreamResource imageResource = new StreamResource(nombreArchivo, () -> getClass().getResourceAsStream(rutaCompleta));
        Image portada = new Image(imageResource, "portada.jpg");
        return portada;
    }

    public Div peliculaBox(String titulo, String valor) {
        Div div = new Div();
        div.addClassName("ficha-pelicula");
        Span labelTitle = new Span(titulo);
        labelTitle.addClassName("label-title");
        Span title = new Span(valor);
        div.add(labelTitle, title);
        return div;
    }

    public String getStars(String valoracion) {
        return switch (valoracion) {
            case "UNAESTRELLA" -> "⭐";
            case "DOSESTRELLAS" -> "⭐".repeat(2);
            case "TRESESTRELLAS" -> "⭐".repeat(3);
            case "CUATROESTRELLAS" -> "⭐".repeat(4);
            case "CINCOESTRELLAS" -> "⭐".repeat(5);
            default -> "⭐";
        };
    }


}
