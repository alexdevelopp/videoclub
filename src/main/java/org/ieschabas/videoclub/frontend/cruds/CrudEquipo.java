package org.ieschabas.videoclub.frontend.cruds;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.ieschabas.videoclub.backend.entities.Actor;
import org.ieschabas.videoclub.backend.entities.Director;
import org.ieschabas.videoclub.backend.entities.Equipo;
import org.ieschabas.videoclub.backend.service.EquipoService;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class CrudEquipo extends VerticalLayout {

    private final EquipoService equipoService;
    private ConfirmDialog confirm;
    private Button cancelConfirm;

    private List<Equipo> coleccion;

    private Grid<Equipo> grid;

    private Equipo equipo;
    private Button addEquipo;
    private Button cancelForm;

    private HorizontalLayout buttons;

    private TextField nombre;
    private TextField apellidos;
    private IntegerField anyoNacimiento;
    private TextField pais;
    private ConfirmDialog confirmCampos;
    private String rol;
    private Boolean isEdit;
    private FormLayout form;


    public CrudEquipo(EquipoService equipoService, String rol, List<Equipo> coleccion) throws SQLException {
        super();
        this.rol = rol;
        this.equipoService = equipoService;
        this.coleccion = coleccion;
        isEdit = false;
        menuBusqueda();
        createGrid();
        createForm();
    }


    private void createGrid() throws SQLException {
        grid = new Grid<>();
        grid.setItems(coleccion);
        grid.addColumn(Equipo::getIdEquipo).setHeader("ID");
        grid.addColumn(Equipo::getNombre).setHeader("Nombre");
        grid.addColumn(Equipo::getApellidos).setHeader("Apellidos");
        grid.addColumn(Equipo::getFecha_nacimiento).setHeader("Año de nacimiento");
        grid.addColumn(Equipo::getPais).setHeader("Pais");
        grid.addColumn(
                //Añado en el grid el boton para eliminar
                new ComponentRenderer<>(Button::new, (button, equipo) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    /*Listener que te pide confirmacion antes de eliminar*/
                    button.addClickListener(e -> {
                        confirm = new ConfirmDialog();
                        confirm.setText("Estas seguro que deseas eliminar?");
                        confirm.setConfirmText("Eliminar");
                        confirm.setCancelButton(cancelConfirm = new Button("Cancelar"));
                        cancelConfirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
                        confirm.setCancelable(true);
                        confirm.add(cancelConfirm);
                        confirm.open();
                        /*Listener que elimina del grid el objeto*/
                        confirm.addConfirmListener(event -> {
                            if (equipo == null)
                                return;
                            deleteEquipo(equipo);
                            isEdit = false;
                        });
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                }));
        grid.addColumn(
                //Añado en el grid el boton para editar
                new ComponentRenderer<>(Button::new, (button, equipo) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    /*Listener que te pide confirmacion antes de editar*/
                    button.addClickListener(e -> {
                        setEquipo(equipo);
                    });
                    button.setIcon(new Icon(VaadinIcon.EDIT));
                }));
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_NO_BORDER);
        add(grid);
    }


    public void setItemsEquipo(List<Equipo> coleccion) {
        grid.setItems(coleccion);
    }


    //Form

    private void createForm() {
        form = new FormLayout();
        nombre = new TextField("Nombre:");
        apellidos = new TextField("Apellidos:");
        anyoNacimiento = new IntegerField("Año de nacimiento:");
        pais = new TextField("Pais");
        addEquipo = new Button("Añadir");
        cancelForm = new Button("Cancelar");
        buttons = createButtonsForm();
        form.add(nombre, apellidos, anyoNacimiento, pais, buttons);
        add(form);
    }

    public HorizontalLayout createButtonsForm() {
        addEquipo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //Funcion del boton addEquipo
        addEquipo.addClickListener(event -> {
            if (rol.equalsIgnoreCase("actor")) {
                //esta variable indica si es un nuevo actor/director o es para modificarlo
                if (isEdit) {
                    if (!camposOk()) return;
                    equipo.setNombre(nombre.getValue());
                    equipo.setApellidos(apellidos.getValue());
                    equipo.setFecha_nacimiento(anyoNacimiento.getValue());
                    equipo.setPais(pais.getValue());
                    try {
                        equipoService.save(equipo);
                    } catch (Exception e) {
                        Notification.show(e.getMessage());
                    }
                    setItemsEquipo(coleccion);
                    emptyField();
                    return;
                }
                if (!camposOk()) return;
                Equipo actor = new Actor(nombre.getValue(), apellidos.getValue(), anyoNacimiento.getValue(), pais.getValue(), rol);
                try {
                    equipoService.save(actor);
                } catch (Exception e) {
                    Notification.show(e.getMessage());
                }
                coleccion.add(actor);
                setItemsEquipo(coleccion);
                emptyField();
            } else {
                //esta variable indica si es un nuevo actor/director o es para modificarlo
                if (isEdit) {
                    if (!camposOk()) return;
                    equipo.setNombre(nombre.getValue());
                    equipo.setApellidos(apellidos.getValue());
                    equipo.setFecha_nacimiento(anyoNacimiento.getValue());
                    equipo.setPais(pais.getValue());
                    try {
                        equipoService.save(equipo);
                    } catch (Exception e) {
                        Notification.show(e.getMessage());
                    }
                    setItemsEquipo(coleccion);
                    emptyField();
                    return;
                }
                if (!camposOk()) return;
                Equipo director = new Director(nombre.getValue(), apellidos.getValue(), anyoNacimiento.getValue(), pais.getValue(), rol);
                try {
                    equipoService.save(director);
                } catch (Exception e) {
                    Notification.show(e.getMessage());
                }
                coleccion.add(director);
                setItemsEquipo(coleccion);
                emptyField();
            }
        });


        cancelForm.addThemeVariants(ButtonVariant.LUMO_ERROR);
        //Funcion del cancelForm
        cancelForm.addClickListener(event -> {
            isEdit = false;
            emptyField();
        });

        return new HorizontalLayout(addEquipo, cancelForm);
    }


    private void emptyField() {
        pais.clear();
        apellidos.clear();
        anyoNacimiento.clear();
        nombre.clear();
    }


    private Boolean camposOk() {
        if (nombre.isEmpty() || apellidos.isEmpty() || pais.isEmpty() || anyoNacimiento.isEmpty()) {
            confirmCampos = new ConfirmDialog();
            confirmCampos.setText("No puedes dejar ningun campo vacio.");
            confirmCampos.setConfirmText("Entendido");
            confirmCampos.open();
            return false;
        } else return true;
    }

    public void setEquipo(Equipo originalEquipo) {
        equipo = originalEquipo;
        nombre.setValue(equipo.getNombre());
        apellidos.setValue(equipo.getApellidos());
        pais.setValue(equipo.getPais());
        anyoNacimiento.setValue(equipo.getFecha_nacimiento());
        isEdit = true;
    }

    private void deleteEquipo(Equipo equipo) {
        try {
            equipoService.delete(equipo.getIdEquipo());
            coleccion.remove(equipo);
            setItemsEquipo(coleccion);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private void createBusqueda(String busqueda) {
        Iterator<Equipo> iterator = coleccion.iterator();
        Equipo equipo;
        while (iterator.hasNext()) {
            equipo = iterator.next();
            if (equipo.getNombre().equalsIgnoreCase(busqueda)) {
                grid.select(equipo);
                if (equipo.getIdEquipo() != 1)
                    grid.scrollToIndex(equipo.getIdEquipo());
                break;
            }
            if (!iterator.hasNext()) {
                Notification.show("No se ha encontrado la busqueda.");
            }
        }
    }

    private void menuBusqueda() {
        HorizontalLayout menu = new HorizontalLayout();
        TextField tituloABuscar = new TextField();
        Button searchButton = new Button("Buscar");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Label menuLabel = new Label("Introduce el nombre que quieres buscar: ");
        menuLabel.getStyle().set("color", "blue");
        menu.add(menuLabel, tituloABuscar, searchButton);
        menu.addClassName("menu-busqueda");
        add(menu);
        searchButton.addClickListener(e -> {
            createBusqueda(tituloABuscar.getValue());
            tituloABuscar.clear();
        });
    }

}
