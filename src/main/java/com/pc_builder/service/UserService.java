package com.pc_builder.service;

import com.pc_builder.entity.auth.AuthUser;

public interface UserService {
    AuthUser getByName(String name);
}
