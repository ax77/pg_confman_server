package ars.srv.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ars.srv.entity.AuthRole;
import ars.srv.repository.AuthRoleRepository;
import ars.srv.service.RoleService;

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

}
