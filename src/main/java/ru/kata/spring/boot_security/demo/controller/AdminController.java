package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;


@Controller
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String printUsers(Model model) {
        model.addAttribute("allUsers", userService.getListOfUsers());
        return "tableOfUsers";
    }

    @GetMapping("/admin/single_user")
    public String showUserById(Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "singleUser";
    }

    @GetMapping("/admin/new_user")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "addUser";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user,
                           BindingResult bindingResult, @RequestParam(value = "roleId", required = false)
                           List<Long> roleId) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }

        List<Role> selectedRoles = roleService.findAllRoleIds(roleId);
        user.getRoles().addAll(selectedRoles);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "updateUser";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user, Long id) {
        userService.updateUser(user, id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete_user")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
