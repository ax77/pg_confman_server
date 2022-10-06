package com.pc_builder.service.auth;

import java.util.List;
import java.util.Optional;

import com.pc_builder.entity.auth.AuthRole;

public interface RoleService {
	Optional<AuthRole> getById(Long id);

	List<AuthRole> getRolesAvailableForUser(Long userId);

	AuthRole getByName(String name);
}
