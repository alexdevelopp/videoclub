package org.ieschabas.videoclub.frontend.cruds;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.apache.commons.compress.utils.IOUtils;
import org.ieschabas.videoclub.backend.entities.Equipo;
import org.ieschabas.videoclub.backend.entities.Pelicula;
import org.ieschabas.videoclub.backend.enums.Categoria;
import org.ieschabas.videoclub.backend.enums.Formato;
import org.ieschabas.videoclub.backend.enums.Valoracion;
import org.ieschabas.videoclub.backend.service.EquipoService;
import org.ieschabas.videoclub.backend.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CrudPelicula extends VerticalLayout {

    @Autowired
    private PeliculaService peliculaService;

    //campos formulario

    private final String ACTOR = "actor";
    private final String DIRECTOR = "director";

    private FormLayout form;
    private MultiSelectComboBox<Equipo> selectActor;
    private MultiSelectComboBox<Equipo> selectDirector;
    private IntegerField id;
    private TextField titulo;
    private TextField duracion;
    private IntegerField anyoPublicacion;
    private Select<Categoria> categoria;
    private Select<Formato> formato;
    private Select<Valoracion> valoracion;
    private TextArea descripcion;
    private Pelicula pelicula;
    private Button addPelicula;
    private Button cancelForm;

    private ConfirmDialog confirmCampos;
    private HorizontalLayout buttonLayoutForm;
    private List<Equipo> listaActores;
    private List<Equipo> listaDirectores;

    private Set<Equipo> actoresSeleccionados;

    private Set<Equipo> directoresSeleccionados;

    private EquipoService equipoService;


    //campos grid
    private ConfirmDialog confirm;
    private Button cancelConfirm;
    private Grid<Pelicula> grid;
    private List<Pelicula> coleccion;
    private String rutaPortada = "/images/";
    private Boolean isEdit;
    private Upload upload;

    public CrudPelicula(PeliculaService peliculaService, EquipoService equipoService, List<Pelicula> coleccion) {
        super();
        this.peliculaService = peliculaService;
        this.equipoService = equipoService;
        this.coleccion = coleccion;
        isEdit = false;
        menuBusqueda();
        createGrid();
        createForm();
    }


    public void createGrid() {
        grid = new Grid<>();
        grid.setItems(coleccion);
        grid.addColumn(Pelicula::getIdPelicula).setHeader("ID");
        grid.addColumn(Pelicula::getTitulo).setHeader("Titulo").setAutoWidth(true);
        grid.addColumn(Pelicula::getDescripcion).setHeader("Descripcion").setAutoWidth(true);
        grid.addColumn(Pelicula::getAnyoPublicacion).setHeader("Año de estreno").setAutoWidth(true);
        grid.addColumn(Pelicula::getCategoria).setHeader("Categoria");
        grid.addColumn(Pelicula::getFormato).setHeader("Formato");
        grid.addColumn(new ComponentRenderer<>(pelicula -> {
            HorizontalLayout layout = new HorizontalLayout();
            for (Equipo actor : pelicula.getActores()) {
                layout.add(new Label(actor.getNombre() + " " + actor.getApellidos()));
            }
            return layout;
        })).setHeader("Actores").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(pelicula -> {
            HorizontalLayout layout = new HorizontalLayout();
            for (Equipo director : pelicula.getDirectores()) {
                layout.add(new Label(director.getNombre() + " " + director.getApellidos()));
            }
            return layout;
        })).setHeader("Directores").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(pelicula -> {
            HorizontalLayout contEstrellas = new HorizontalLayout();
            contEstrellas.add(getStars(pelicula.getValoracion().toString()));
            return contEstrellas;
        })).setHeader("Valoracion").setAutoWidth(true);

        grid.addColumn(
                //Añado en el grid el boton para eliminar
                new ComponentRenderer<>(Button::new, (button, pelicula) -> {
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
                            if (pelicula == null)
                                return;
                            deletePelicula(pelicula);
                            emptyField();
                            addPelicula.setText("Añadir");
                        });
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                }));
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, pelicula) -> {
                    button.addClickListener(e -> {
                        setFieldsPelicula(pelicula);
                    });
                    button.setIcon(new Icon(VaadinIcon.EDIT));
                }));

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_NO_BORDER);
        add(grid);
    }


    /**
     * Actualiza el grid cuando hay algun cambio
     */
    public void setPeliculas(List<Pelicula> coleccion) {
        grid.setItems(coleccion);
    }

    //FORMULARIO

    public void createForm() {
        form = new FormLayout();
        id = new IntegerField("ID:");
        titulo = new TextField("Titulo:");
        duracion = new TextField("Duracion:");
        anyoPublicacion = new IntegerField("Año de publicacion:");
        categoria = new Select<>();
        categoria.setLabel("Categoria:");
        categoria.setItems(Categoria.values());
        formato = new Select<>();
        formato.setLabel("Formato:");
        formato.setItems(Formato.values());
        valoracion = new Select<>();
        valoracion.setLabel("Valoracion:");
        valoracion.setItems(Valoracion.values());
        selectActor = new MultiSelectComboBox<>("Actores");
        cargarActores();
        selectDirector = new MultiSelectComboBox<>("Directores");
        cargarDirectores();

        //subir portada de la pelicula
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".jpg");
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            guardarImagen(inputStream, fileName);
        });

        descripcion = new TextArea("Descripcion");
        addPelicula = new Button("Añadir");
        cancelForm = new Button("Cancelar");
        buttonLayoutForm = createButtonsForm();
        form.add(titulo, anyoPublicacion, duracion, categoria, selectActor, selectDirector, formato, valoracion, descripcion, upload, buttonLayoutForm);
        add(form);
    }


    public HorizontalLayout createButtonsForm() {
        addPelicula.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //Evento que crea/modifica una pelicula
        addPelicula.addClickListener(event -> {
            if (isEdit) {
                if (!camposOk()) return;
                pelicula.setTitulo(titulo.getValue());
                pelicula.setAnyoPublicacion(anyoPublicacion.getValue());
                pelicula.setDuracion(duracion.getValue());
                pelicula.setCategoria(categoria.getValue());
                pelicula.setFormato(formato.getValue());
                pelicula.setValoracion(valoracion.getValue());
                pelicula.setActores(actoresSeleccionados = selectActor.getSelectedItems());
                pelicula.setDirectores(directoresSeleccionados = selectDirector.getSelectedItems());
                pelicula.setDescripcion(descripcion.getValue());
                pelicula.setPortada(rutaPortada);
                try {
                    peliculaService.save(pelicula);
                } catch (Exception exception) {
                    Notification.show(exception.getMessage());
                }
                setPeliculas(coleccion);
                emptyField();
                isEdit = false;
                return;
            }
            if (!camposOk()) return;
            actoresSeleccionados = selectActor.getSelectedItems();
            directoresSeleccionados = selectDirector.getSelectedItems();
            pelicula = new Pelicula(titulo.getValue(), anyoPublicacion.getValue(), duracion.getValue(), categoria.getValue(), formato.getValue(), valoracion.getValue(), descripcion.getValue());
            pelicula.setActores(actoresSeleccionados);
            pelicula.setDirectores(directoresSeleccionados);
            pelicula.setPortada(rutaPortada);
            try {
                peliculaService.save(pelicula);
            } catch (Exception e) {
                Notification.show(e.getMessage());
            }
            coleccion.add(pelicula);
            setPeliculas(coleccion);
            emptyField();
        });

        cancelForm.addThemeVariants(ButtonVariant.LUMO_ERROR);
        //Funcion del cancelForm
        cancelForm.addClickListener(event -> {
            isEdit = false;
            addPelicula.setText("Añadir");
            emptyField();
        });

        return new HorizontalLayout(addPelicula, cancelForm);
    }

    /**
     * Reinicia los campos del formulario
     */
    private void emptyField() {
        titulo.clear();
        duracion.clear();
        anyoPublicacion.clear();
        categoria.clear();
        formato.clear();
        selectActor.clear();
        selectDirector.clear();
        valoracion.clear();
        descripcion.clear();
        upload.clearFileList();
    }

    /**
     * Validacion para no enviar campos vacios en el form
     */
    private Boolean camposOk() {
        if (titulo.isEmpty()
                || duracion.isEmpty()
                || anyoPublicacion.isEmpty()
                || categoria.isEmpty()
                || formato.isEmpty()
                || selectActor.isEmpty()
                || selectDirector.isEmpty()
                || descripcion.isEmpty()
                || valoracion.isEmpty()) {
            confirmCampos = new ConfirmDialog();
            confirmCampos.setText("No puedes dejar ningun campo vacio.");
            confirmCampos.setConfirmText("Entendido");
            confirmCampos.open();
            return false;
        }
        return true;
    }

    /**
     * Cargar multi-select
     */
    private void cargarActores() {
        try {
            listaActores = equipoService.getAll(ACTOR);
            selectActor.setItemLabelGenerator(Equipo -> Equipo.getNombre() + " " + Equipo.getApellidos());
            selectActor.setItems(listaActores);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    private void cargarDirectores() {
        try {
            listaDirectores = equipoService.getAll(DIRECTOR);
            selectDirector.setItemLabelGenerator(Equipo -> Equipo.getNombre() + " " + Equipo.getApellidos());
            selectDirector.setItems(listaDirectores);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    /**
     * Metodo que elimina una pelicula
     *
     * @param pelicula
     */

    private void deletePelicula(Pelicula pelicula) {
        try {
            peliculaService.delete(pelicula.getIdPelicula());
            coleccion.remove(pelicula);
            setPeliculas(coleccion);
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }
    }

    /**
     * Metodo que setea los campos del formulario para editar una pelicula
     *
     * @param peliculaOriginal
     */
    private void setFieldsPelicula(Pelicula peliculaOriginal) {
        isEdit = true;
        addPelicula.setText("Guardar");
        pelicula = peliculaOriginal;
        titulo.setValue(pelicula.getTitulo());
        duracion.setValue(pelicula.getDuracion());
        anyoPublicacion.setValue(pelicula.getAnyoPublicacion());
        categoria.setValue(pelicula.getCategoria());
        formato.setValue(pelicula.getFormato());
        valoracion.setValue(pelicula.getValoracion());
        selectActor.setValue(pelicula.getActores());
        selectDirector.setValue(pelicula.getDirectores());
        descripcion.setValue(pelicula.getDescripcion());
    }

    private String getStars(String valoracion) {
        return switch (valoracion) {
            case "UNAESTRELLA" -> "⭐";
            case "DOSESTRELLAS" -> "⭐".repeat(2);
            case "TRESESTRELLAS" -> "⭐".repeat(3);
            case "CUATROESTRELLAS" -> "⭐".repeat(4);
            case "CINCOESTRELLAS" -> "⭐".repeat(5);
            default -> "⭐";
        };
    }

    private void createBusqueda(String busqueda) {
        Iterator<Pelicula> iterator = coleccion.iterator();
        Pelicula pelicula;
        while (iterator.hasNext()) {
            pelicula = iterator.next();
            if (pelicula.getTitulo().equalsIgnoreCase(busqueda)) {
                grid.select(pelicula);
                if (pelicula.getIdPelicula() != 1)
                    grid.scrollToIndex(pelicula.getIdPelicula());
                break;
            }
            if (!iterator.hasNext()) {
                Notification.show("No se ha encontrado la pelicula.");
            }
        }
    }

    private void menuBusqueda() {
        HorizontalLayout menu = new HorizontalLayout();
        TextField tituloABuscar = new TextField();
        Button searchButton = new Button("Buscar");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Label menuLabel = new Label("Introduce el titulo de la pelicula: ");
        menuLabel.getStyle().set("color", "blue");
        menu.add(menuLabel, tituloABuscar, searchButton);
        menu.addClassName("menu-busqueda");
        add(menu);
        searchButton.addClickListener(e -> {
            createBusqueda(tituloABuscar.getValue());
            tituloABuscar.clear();
        });
    }

    //subir portada
    private void guardarImagen(InputStream inputStream, String fileName) {
        Path path = Paths.get("src/main/resources/images/" + fileName);
        File archivoImagen = path.toFile();
        try (OutputStream outputStream = new FileOutputStream(archivoImagen)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rutaPortada = rutaPortada + fileName;
    }

}
