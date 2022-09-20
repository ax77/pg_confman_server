package com.pc_builder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc_builder.entity.auth.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByName(String name);
}
