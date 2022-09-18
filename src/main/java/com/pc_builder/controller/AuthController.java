package com.pc_builder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pc_builder.service.RoleService;

@Controller
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 7200)
public class AuthController {

	private RoleService roleService;

	@Autowired
	public AuthController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return new ResponseEntity<>(roleService.getRolesAvailableForUser(1L), HttpStatus.OK);
	}

}
