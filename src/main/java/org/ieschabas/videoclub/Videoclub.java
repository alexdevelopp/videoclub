package org.ieschabas.videoclub;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */


@Theme(value = "mytodo")
@Configuration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories("org.ieschabas.videoclub.backend.repositories")
@ComponentScan("org.ieschabas.videoclub.*")
public class Videoclub implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Videoclub.class, args);
    }

}
