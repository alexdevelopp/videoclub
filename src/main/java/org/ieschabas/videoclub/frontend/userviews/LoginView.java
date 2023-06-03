package org.ieschabas.videoclub.frontend.userviews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.ieschabas.videoclub.backend.entities.Usuario;
import org.ieschabas.videoclub.backend.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;


@PageTitle("VIDEOCLUB")
@Route("login")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private SecurityService securityService;

    private LoginForm login = new LoginForm();

    private Usuario usuario;
    private Button signIn;


    public LoginView(SecurityService securityService) {
        this.securityService = securityService;
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header-class");
        HorizontalLayout titleContainer = new HorizontalLayout();
        titleContainer.addClassName("title-container");
        H1 title = new H1("VIDEOCLUB");
        title.addClassName("title");
        titleContainer.add(title);
        header.add(titleContainer);
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        signIn = new Button("Registrarse");
        signIn.addClassName("login-button");

        signIn.addClickListener(e -> {
            UI.getCurrent().navigate(SignInView.class);
        });

        addClassName("body-login");
        add(header, login, signIn);
    }

    //Muestra error si la validacion del usuario es incorrecta.
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            Notification error = new Notification("El usuario o la contraseña no coincide. Por favor, vuelve a intentarlo.");
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.setDuration(2000);
            error.open();
        }
    }

}


