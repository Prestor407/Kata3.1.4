package ru.kata.spring.boot_security.demo.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepo;
import ru.kata.spring.boot_security.demo.repositories.UserRepo;

import javax.annotation.PostConstruct;

@Component
@Transactional
public class Init {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;;

    @Autowired
    public Init(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleRepo.save(adminRole);
        roleRepo.save(userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("100"));
        admin.setAge((byte) 10);
        admin.setEmail("admin@gmail.com");
        admin.getRoles().add(adminRole);
        admin.getRoles().add(userRole);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("100"));
        user.setAge((byte) 10);
        user.setEmail("user@gmail.com");
        user.getRoles().add(userRole);

        userRepo.save(user);
        userRepo.save(admin);
    }
}
