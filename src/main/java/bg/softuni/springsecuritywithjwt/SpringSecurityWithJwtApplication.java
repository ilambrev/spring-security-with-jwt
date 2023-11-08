package bg.softuni.springsecuritywithjwt;

import bg.softuni.springsecuritywithjwt.model.dto.RegisterRequest;
import bg.softuni.springsecuritywithjwt.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static bg.softuni.springsecuritywithjwt.model.enums.Role.ADMIN;
import static bg.softuni.springsecuritywithjwt.model.enums.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SpringSecurityWithJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityWithJwtApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {
        return args -> {
            RegisterRequest admin = new RegisterRequest()
                    .setFirstName("Admin")
                    .setLastName("Adminov")
                    .setEmail("admin@test.bg")
                    .setPassword("secret")
                    .setRole(ADMIN);

            System.out.println("Admin token: " + authenticationService.register(admin).getAccessToken());

            RegisterRequest manager = new RegisterRequest()
                    .setFirstName("Manager")
                    .setLastName("Managerov")
                    .setEmail("manager@test.bg")
                    .setPassword("secret")
                    .setRole(MANAGER);

            System.out.println("Manager token: " + authenticationService.register(manager).getAccessToken());
        };

    }

}
