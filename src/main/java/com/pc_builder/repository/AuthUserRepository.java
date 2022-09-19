package com.pc_builder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pc_builder.entity.AuthRole;
import com.pc_builder.entity.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
	AuthUser findByName(String name);
}
