package com.pc_builder.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pc_builder.entity.AuthUser;
import com.pc_builder.repository.AuthUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AuthUserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " is not exists");
        }
        return UserDetailsImpl.buildUserDetailsImplFromAuthUser(user);
    }

}
