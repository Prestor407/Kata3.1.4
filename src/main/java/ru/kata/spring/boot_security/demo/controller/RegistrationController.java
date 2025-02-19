package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;


@Controller
public class RegistrationController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public RegistrationController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") User user, List<Long> roleIds) {
        List<Role> selectedRoles = roleService.findAllRoleIds(roleIds);
        user.getRoles().addAll(selectedRoles);
        userService.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHomePage(@ModelAttribute("user") User user) {
        return "homepage";
    }

}
