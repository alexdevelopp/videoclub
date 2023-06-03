package org.ieschabas.videoclub.frontend.adminviews;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.ieschabas.videoclub.backend.entities.Equipo;
import org.ieschabas.videoclub.frontend.cruds.CrudEquipo;
import org.ieschabas.videoclub.backend.service.EquipoService;
import org.ieschabas.videoclub.frontend.MainView;
import org.ieschabas.videoclub.frontend.styles.Components;

import java.sql.SQLException;
import java.util.List;


@Route(value = "equipo", layout = MainView.class)
@PageTitle("Equipo")
@RolesAllowed("administrador")
public class EquipoView extends VerticalLayout implements HasUrlParameter<String> {

    //rol
    private final String ACTOR = "actor";

    private final String DIRECTOR = "director";

    //entities
    private final EquipoService equipoService;

    CrudEquipo crud = null;


    //crud
    protected Grid<Equipo> grid;


    //estilos

    protected AppLayout menu;
    protected H2 title;
    protected Components tabs;

    //listas
    protected List<Equipo> items;


    public EquipoView(EquipoService equipoService) throws SQLException {
        super();
        this.equipoService = equipoService;
        title = new H2();
        add(title);
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        var titulo = s.equalsIgnoreCase("actor") ? "Listado de actores" : "Listado de directores";
        title.setText(titulo);
        items = equipoService.getAll(s);
        if (crud == null) {
            try {
                crud = new CrudEquipo(equipoService, s, items);
                add(crud);
            } catch (SQLException e) {
                Notification.show(e.getMessage());
            }
        } else crud.setItemsEquipo(items);
    }
}

