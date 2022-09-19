package com.pc_builder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pc_builder.entity.AuthRole;
import com.pc_builder.entity.AuthRoleResourcePrivilege;
import com.pc_builder.entity.AuthUser;
import com.pc_builder.service.RoleService;
import com.pc_builder.service.UserService;

@Controller
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 7200)
public class AuthController {

    private RoleService roleService;
    private UserService userService;

    @Autowired
    public AuthController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
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

    @GetMapping("/users")
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
