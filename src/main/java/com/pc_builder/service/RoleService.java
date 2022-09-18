package com.pc_builder.service;

import java.util.List;
import java.util.Optional;

import com.pc_builder.entity.AuthRole;

public interface RoleService {
	Optional<AuthRole> getById(Long id);
	List<AuthRole> getRolesAvailableForUser(Long userId);
	AuthRole getByName(String name);
}
