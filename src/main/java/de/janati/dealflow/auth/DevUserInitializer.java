package de.janati.dealflow.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// DEV ONLY: creates demo users. Remove or guard with a profile before production.
@Configuration
public class DevUserInitializer {

    @Bean
    CommandLineRunner createDemoUsers(AppUserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (users.findByUsername("sales").isEmpty()) {
                users.save(new AppUser("sales", encoder.encode("sales123"), Role.SALES));
            }
            if (users.findByUsername("manager").isEmpty()) {
                users.save(new AppUser("manager", encoder.encode("manager123"), Role.MANAGER));
            }
        };
    }
}