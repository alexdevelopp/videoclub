package org.ieschabas.videoclub.frontend.styles;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.ieschabas.videoclub.backend.service.UserService;


public class Header extends HorizontalLayout {

    private Usuario usuario;

    private final SecurityService securityService;

    private final UserService userService;


    public Header(SecurityService securityService, UserService userService) {
        super();
        this.securityService = securityService;
        this.userService = userService;
        var usrDetails = securityService.getAuthenticatedUser();
        usuario = userService.findByMail(usrDetails.getUsername());
        addClassName("header-class");
        createHeader();
    }

    public void createHeader() {
        HorizontalLayout titleContainer = new HorizontalLayout();
        titleContainer.addClassName("title-container");
        H1 title = new H1("VIDEOCLUB");
        title.addClassName("title");
        titleContainer.add(title);
        if (usuario != null) {
            HorizontalLayout logoContainer = new HorizontalLayout();
            logoContainer.addClassName("container-logo");
            Button login = new Button();
            login.addClassName("button");
            login.setIcon(VaadinIcon.SIGN_OUT.create());
            Label nameUser = new Label(usuario.getNombre() + " " + usuario.getApellidos());
            nameUser.addClassName("label-user");
            logoContainer.add(login, nameUser);

            login.addClickListener(e -> {
                ConfirmDialog confirmLogout = new ConfirmDialog();
                confirmLogout.setText("Esta seguro de que quiere cerrar la sesion?");
                confirmLogout.setConfirmText("Cerrar sesion");
                Button cancelConfirm = new Button("Cancelar");
                confirmLogout.setCancelButton(cancelConfirm);
                cancelConfirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
                confirmLogout.setCancelable(true);
                confirmLogout.add(cancelConfirm);
                confirmLogout.open();

                confirmLogout.addConfirmListener(event -> {
                    securityService.logout();
                });

                confirmLogout.addCancelListener(event -> {
                    confirmLogout.close();
                });

            });
            add(titleContainer, logoContainer);
            return;
        }
        add(titleContainer);
    }


}
