package com.pc_builder.service;

import java.util.List;
import java.util.Optional;

import com.pc_builder.entity.AuthRole;
import com.pc_builder.entity.AuthUser;

public interface UserService {
	AuthUser getByName(String name);
}
