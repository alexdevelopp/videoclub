package org.ieschabas.videoclub.frontend.adminviews;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.ieschabas.videoclub.frontend.cruds.CrudPelicula;
import org.ieschabas.videoclub.backend.service.EquipoService;
import org.ieschabas.videoclub.backend.service.PeliculaService;
import org.ieschabas.videoclub.frontend.MainView;

import java.io.IOException;
import java.util.List;


@PageTitle("Peliculas")
@Route(value = "peliculas", layout = MainView.class)
@RolesAllowed("administrador")

public class PeliculasView extends VerticalLayout {

    //servicios

    private final PeliculaService peliculaService;
    private final EquipoService equipoService;


    //rol
    private final String ACTOR = "actor";

    private final String DIRECTOR = "director";

    private H2 title;

    //colecciones
    private List<Pelicula> coleccionPelis;

    private CrudPelicula crudPelicula;


    public PeliculasView(EquipoService equipoService, PeliculaService peliculaService) throws IOException {
        this.peliculaService = peliculaService;
        this.equipoService = equipoService;
        coleccionPelis = peliculaService.getAll();
        crudPelicula = new CrudPelicula(peliculaService, equipoService, coleccionPelis);
        title = new H2("Listado de peliculas");
        add(title, crudPelicula);
    }


}