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
import ru.kata.spring.boot_security.demo.repositories.UserRepo;


import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleService;


    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleServiceImpl roleService) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleService = roleService;
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }


    @Override
    public void addUser(User user, List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (roleIds == null || roleIds.isEmpty()) {
            user.setRoles(Collections.singleton(roleService.findByName("ROLE_USER")));
        } else {
            Set<Role> roles = new HashSet<>(roleService.findAllRoleIds(roleIds));
            user.setRoles(roles);
        }
        userRepo.save(user);
    }


    @Override
    public void updateUser(User user, Long userId) {
        User updatedUser = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(updatedUser.getPassword());
        }
        userRepo.save(user);
    }


    @Override
    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepo.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getListOfUsers() {
        return userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }


}
