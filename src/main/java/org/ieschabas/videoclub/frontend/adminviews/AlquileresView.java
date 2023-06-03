package org.ieschabas.videoclub.frontend.adminviews;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Alquiler;
import org.ieschabas.videoclub.backend.service.AlquilerService;
import org.ieschabas.videoclub.backend.service.PeliculaService;
import org.ieschabas.videoclub.frontend.MainView;

import java.util.List;

@Route(value = "alquileres", layout = MainView.class)
@PageTitle("Historico de alquileres")
@RolesAllowed("administrador")
public class AlquileresView extends VerticalLayout {

    private final AlquilerService alquilerService;
    private final PeliculaService peliculaService;
    private List<Alquiler> alquileres;
    private Grid<Alquiler> grid;


    public AlquileresView(AlquilerService alquilerService, PeliculaService peliculaService) {
        super();
        this.alquilerService = alquilerService;
        this.peliculaService = peliculaService;
        alquileres = alquilerService.getAll();
        grid = createGrid();
        H2 title = new H2("Historico de alquileres");
        add(title, grid);
    }

    private Grid<Alquiler> createGrid() {
        Grid<Alquiler> grid = new Grid<>();
        grid.setItems(alquileres);
        grid.addColumn(Alquiler::getIdAlquiler).setHeader("ID alquiler");
        grid.addColumn(alquiler -> alquiler.getAlquilerPK().getCliente().getNombre()).setHeader("Cliente");
        grid.addColumn(alquiler -> alquiler.getAlquilerPK().getPelicula().getTitulo()).setHeader("Pelicula");
        grid.addColumn(Alquiler::getFechaAlquiler).setHeader("Fecha de alquiler");
        grid.addColumn(alquiler -> alquiler.getAlquilerPK().getFechaDevolucion()).setHeader("Fecha de devolucion");
        return grid;
    }

}
