package com.pc_builder.service.impl.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc_builder.entity.auth.AuthUser;
import com.pc_builder.repository.auth.AuthUserRepository;
import com.pc_builder.service.auth.UserService;

@Service
public class UserServiceImpl implements UserService {

	private AuthUserRepository authUserRepository;

	@Autowired
	public UserServiceImpl(AuthUserRepository authUserRepository) {
		this.authUserRepository = authUserRepository;
	}

	@Override
	public AuthUser getByName(String name) {
		return this.authUserRepository.findByName(name);
	}

	@Override
	public AuthUser save(AuthUser user) {
		return authUserRepository.save(user);
	}

	@Override
	public List<AuthUser> getAll() {
		return authUserRepository.findAll();
	}
}
