package ars.srv.service;

import java.util.List;
import java.util.Optional;

import ars.srv.entity.AuthRole;

public interface RoleService {
	Optional<AuthRole> getById(Long id);
	List<AuthRole> getRolesAvailableForUser(Long userId);
}
