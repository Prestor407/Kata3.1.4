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

import java.security.Principal;
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
    public String printUsers(Model model, Principal principal) {
        model.addAttribute("allUsers", userService.getListOfUsers());
        User curUser;
        curUser = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("curUser", curUser);
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "admin_page";
    }


    @GetMapping("/admin/single_user")
    public String showUserById(Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user_page";
    }


    @PostMapping("/admin/delete_user")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("updatedUser") User user,
                             @RequestParam(value = "roleId", required = false) List<Long> roleId) {
        List<Role> selectedRoles = roleService.findAllRoleIds(roleId);
        user.getRoles().addAll(selectedRoles);
        userService.updateUser(user, user.getId());
        return "redirect:/admin";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute("user") User user,
                           BindingResult bindingResult, @RequestParam(value = "roleIds", required = false)
                           List<Long> roleId) {
        if (bindingResult.hasErrors()) {
            return "admin_page";
        }

        List<Role> selectedRoles = roleService.findAllRoleIds(roleId);
        user.getRoles().addAll(selectedRoles);
        userService.addUser(user);
        return "redirect:/admin";
    }
}
