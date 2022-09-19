package com.pc_builder.service;

import com.pc_builder.entity.AuthUser;

public interface UserService {
    AuthUser getByName(String name);
}
