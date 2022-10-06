package com.pc_builder.service.impl.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc_builder.entity.auth.AuthRole;
import com.pc_builder.repository.auth.AuthRoleRepository;
import com.pc_builder.service.auth.RoleService;

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
