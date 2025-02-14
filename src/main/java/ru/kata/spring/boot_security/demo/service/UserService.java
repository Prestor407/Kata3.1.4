package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepo;
import ru.kata.spring.boot_security.demo.repositories.UserRepo;



import java.util.Collections;
import java.util.List;


@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleRepo = roleRepo;
    }


    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepo.findByName("ROLE_USER")));
        userRepo.save(user);
    }


    public void updateUser(User user) {
        userRepo.save(user);
    }


    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    public void saveRole(Role role) {
        roleRepo.save(role);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepo.getById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getListOfUsers() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
