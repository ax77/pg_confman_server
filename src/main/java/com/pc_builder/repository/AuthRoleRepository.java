package com.pc_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pc_builder.entity.AuthRole;

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {

	Optional<AuthRole> findById(Long id);
	
	@Query(value = "select r.auth_role_id, r.auth_role_name from auth_role r where r.auth_role_name = ?1", nativeQuery = true)
	AuthRole findByName(String name);
	
	@Query(value = 
			"   select uhr.auth_role_id, r.auth_role_name "
			+ " from auth_user_has_roles as uhr "
			+ " inner join auth_role as r "
			+ " on uhr.auth_role_id = r.auth_role_id"
			+ " where uhr.auth_user_id = ?1", nativeQuery = true)
	List<AuthRole> findRolesAvailableForUser(Long userId);
	
}
