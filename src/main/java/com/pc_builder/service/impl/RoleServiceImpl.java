package com.pc_builder.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc_builder.entity.auth.AuthRole;
import com.pc_builder.repository.AuthRoleRepository;
import com.pc_builder.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private AuthRoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(AuthRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<AuthRole> getById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<AuthRole> getRolesAvailableForUser(Long userId) {
        return roleRepository.findRolesAvailableForUser(userId);
    }

    @Override
    public AuthRole getByName(String name) {
        return roleRepository.findByName(name);
    }

}
