package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepo;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }


    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepo.findById(id);
    }

    @Override
    public void save(Role role) {
        roleRepo.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public List<Role> findAllRoleIds(List<Long> roleIds) {
        return roleRepo.findAllById(roleIds);
    }
}
