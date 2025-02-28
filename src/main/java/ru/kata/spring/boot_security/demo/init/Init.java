package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
@Transactional
public class Init {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public Init(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleService.save(adminRole);
        roleService.save(userRole);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);


        User admin = new User(
                "Admin",
                "100",
                "admin@mail.ru",
                (byte) 10,
                roles);


        User user = new User("User",
                "100",
                "user@mail.ru",
                (byte) 10,
                Collections.singleton(userRole));


        userService.addUser(admin);
        userService.addUser(user);

    }
}
