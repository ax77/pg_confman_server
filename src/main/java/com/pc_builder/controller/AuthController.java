package com.pc_builder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pc_builder.entity.AuthRole;
import com.pc_builder.entity.AuthRoleResourcePrivilege;
import com.pc_builder.entity.AuthUser;
import com.pc_builder.message.request.LoginRequest;
import com.pc_builder.message.response.JwtResponse;
import com.pc_builder.security.jwt.JwtTokenGenerator;
import com.pc_builder.security.service.UserDetailsImpl;
import com.pc_builder.service.RoleService;
import com.pc_builder.service.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*", maxAge = 7200)
public class AuthController {

    @SuppressWarnings("unused")
    private RoleService roleService;
    
    private UserService userService;

    private AuthenticationManager authManager;
    
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    public AuthController(RoleService roleService, UserService userService, AuthenticationManager authManager,
            JwtTokenGenerator tokenGenerator) {
        this.roleService = roleService;
        this.userService = userService;
        this.authManager = authManager;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenGenerator.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> authorities = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        List<String> roles = userDetails.getRoles();

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles, authorities));
    }

    // We have to build that kind of authorities.
    //
    // [
    // "DOCUMENT_SHOW",
    // "DOCUMENT_DELETE",
    // "DOCUMENT_CREATE",
    // "DOCUMENT_EDIT",
    // "DOCUMENT_INDEX",
    //
    // "PROFESSION_SHOW",
    // "PROFESSION_DELETE",
    // "PROFESSION_CREATE",
    // "PROFESSION_EDIT",
    // "PROFESSION_INDEX",
    //
    // "PERMISSION_SHOW",
    // "PERMISSION_DELETE",
    // "PERMISSION_CREATE",
    // "PERMISSION_EDIT",
    // "PERMISSION_INDEX"
    // ]

    @GetMapping("users")
    public ResponseEntity<?> getUsers() {
        AuthUser user = userService.getByName("admin");
        List<String> result = new ArrayList<>();
        for (AuthRole role : user.getRoles()) {
            for (AuthRoleResourcePrivilege rrp : role.getAuthRoleResourcePrivileges()) {
                String authority = rrp.getResource().getName() + "_" + rrp.getPrivilege().getName();
                result.add(authority.toUpperCase());
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
