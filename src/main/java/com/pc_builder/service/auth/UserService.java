package com.pc_builder.service.auth;

import java.util.List;

import com.pc_builder.entity.auth.AuthUser;

public interface UserService {
	AuthUser getByName(String name);

	AuthUser save(AuthUser user);

	List<AuthUser> getAll();
}
