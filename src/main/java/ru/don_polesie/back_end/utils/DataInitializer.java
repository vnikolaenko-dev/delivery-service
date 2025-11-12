package ru.don_polesie.back_end.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.don_polesie.back_end.model.Role;
import ru.don_polesie.back_end.model.user.User;
import ru.don_polesie.back_end.repository.RoleRepository;
import ru.don_polesie.back_end.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        Role workerRole = roleRepository.findByName("ROLE_WORKER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_WORKER")));


        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));


        if (userRepository.findByPhoneNumber("79919916871").isEmpty()) {
            User admin = new User();
            admin.setName("Денис");
            admin.setSurname("Погосов");
            admin.setPhoneNumber("79919916871");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(adminRole);
            admin.getRoles().add(workerRole);
            admin.getRoles().add(userRole);
            userRepository.save(admin);
            System.out.println("Admin user created: 79919916871 / admin123");
        }
    }
}
