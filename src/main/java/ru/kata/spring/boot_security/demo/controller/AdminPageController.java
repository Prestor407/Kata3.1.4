package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class AdminPageController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminPageController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @GetMapping("/admin")
    public String ShowAdminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("curUser", userService.getUserByUsername(userDetails.getUsername()));
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());

        return "admin_page";
    }
}
