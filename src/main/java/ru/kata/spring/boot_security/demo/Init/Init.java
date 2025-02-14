package ru.kata.spring.boot_security.demo.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;

@Component
@Transactional
public class Init {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;;

    @Autowired
    public Init(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        userService.saveRole(adminRole);
        userService.saveRole(userRole);

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

        userService.saveUser(user);
        userService.saveUser(admin);
    }
}
