package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.Exceptions.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.util.List;



@RestController
@RequestMapping("/api")
public class AdminController {

    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/admin")
    public List<User> printUsers() {
        return userService.getListOfUsers();
    }


    @GetMapping("/admin/single_user/{id}")
    public User showUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user==null) {
            throw new NoSuchUserException("User with id " + id + " not found");
        }
        return user;
    }


    @PostMapping("/admin/save")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }
}
