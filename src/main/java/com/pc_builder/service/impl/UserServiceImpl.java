package com.pc_builder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc_builder.entity.AuthUser;
import com.pc_builder.repository.AuthUserRepository;
import com.pc_builder.service.UserService;

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
}
