package org.ieschabas.videoclub.frontend.userviews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.videoclub.backend.entities.Cliente;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.ieschabas.videoclub.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Route("registro")
@PageTitle("Registro")
@AnonymousAllowed

public class SignInView extends AppLayout {

    private TextField nombre;
    private TextField apellidos;
    private TextField direccion;
    private EmailField email;
    private PasswordField password;

    //buttons
    private Button send;
    private Button cancel;

    private SecurityService securityService;

    //estilos

    @Autowired
    private UserService userService;

    public SignInView(UserService userService) {
        this.userService = userService;

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header-class");
        HorizontalLayout titleContainer = new HorizontalLayout();
        titleContainer.addClassName("title-container");
        H1 title = new H1("VIDEOCLUB");
        title.addClassName("title");
        titleContainer.add(title);
        header.add(titleContainer);

        addToNavbar(header);

        VerticalLayout contenedorForm = createForm();
        setContent(contenedorForm);

        send.addClickListener(e -> createUser());

        cancel.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));
    }

    public VerticalLayout createForm() {

        H2 titleFormUser = new H2("Crea tu usuario");
        //form
        FormLayout form = new FormLayout();
        nombre = new TextField("Nombre:");
        apellidos = new TextField("Apellidos:");
        direccion = new TextField("Direccion:");
        email = new EmailField("Email:");
        password = new PasswordField("Contraseña:");
        //botones
        send = new Button("Crear cuenta");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel = new Button("Cancelar");
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.add(send, cancel);
        form.add(nombre, apellidos, direccion, email, password, buttonsContainer);
        //contenedorForm
        VerticalLayout contForm = new VerticalLayout();
        contForm.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        contForm.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        contForm.getStyle().set("margin", "auto").set("border", "2px solid black").set("width", "60%").set("margin-top", "5%").set("border-radius", "1.313em 2.625em").set("padding", "2.25em");
        contForm.add(titleFormUser, form);
        return contForm;
    }

    private void createUser() {
        if (!camposOk()) return;
        if (email.isInvalid()) {
            Notification errorEmail = new Notification("El email que has introducido no se ajusta al formato correcto.");
            errorEmail.addThemeVariants(NotificationVariant.LUMO_ERROR);
            errorEmail.setDuration(2000);
            errorEmail.open();
            email.clear();
            return;
        }
        try {
            //comprobamos que el usuario no esté ya registrado
            Usuario validation = userService.findByMail(email.getValue());
            //si lo está arrojamos mensaje de error
            if (validation != null) {
                throwError();
            } else {
                //Si no, construimos el user con los valores del formulario
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                Usuario usuario = new Cliente(nombre.getValue(), apellidos.getValue(), direccion.getValue(), LocalDate.now(), email.getValue(), encoder.encode(password.getValue()));
                try {
                    userService.save(usuario);
                } catch (Exception e) {
                    Notification.show(e.getMessage());
                }
                throwNotification();
                UI.getCurrent().navigate(LoginView.class);
            }
        } catch (Exception e) {
            Notification.show(e.getMessage());
        }

    }

    //Notificaciones

    private void throwNotification() {
        Notification notification = new Notification("El usuario se ha creado correctamente.");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(3000);
        notification.open();
    }

    private void throwError() {
        Notification notification = new Notification("El email que has elegido ya esta en uso. Por favor, elige otro.");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(3000);
        notification.open();
    }

    private Boolean camposOk() {
        if (nombre.getValue().isEmpty()
                || apellidos.getValue().isEmpty()
                || password.getValue().isEmpty()
                || direccion.getValue().isEmpty()
                || email.getValue().isEmpty()) {
            ConfirmDialog confirmCampos = new ConfirmDialog();
            confirmCampos.setText("No puedes dejar ningun campo vacio.");
            confirmCampos.setConfirmText("Entendido");
            confirmCampos.open();
            return false;
        }
        return true;
    }
}
