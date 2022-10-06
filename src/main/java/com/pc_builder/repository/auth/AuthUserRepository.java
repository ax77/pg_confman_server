package com.pc_builder.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc_builder.entity.auth.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
	AuthUser findByName(String name);

	List<AuthUser> findAll();
}
