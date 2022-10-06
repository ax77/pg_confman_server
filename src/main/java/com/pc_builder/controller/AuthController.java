package com.pc_builder.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pc_builder.entity.auth.AuthRole;
import com.pc_builder.entity.auth.AuthUser;
import com.pc_builder.message.request.LoginRequest;
import com.pc_builder.message.response.JsonResponse;
import com.pc_builder.message.response.JwtResponse;
import com.pc_builder.security.jwt.JwtTokenGenerator;
import com.pc_builder.security.service.UserDetailsImpl;
import com.pc_builder.service.auth.RoleService;
import com.pc_builder.service.auth.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*", maxAge = 7200)
public class AuthController {

	private RoleService roleService;

	private UserService userService;

	private AuthenticationManager authManager;

	private JwtTokenGenerator tokenGenerator;

	private PasswordEncoder passwordEncoder;

	@Autowired
	public AuthController(RoleService roleService, UserService userService, AuthenticationManager authManager,
			JwtTokenGenerator tokenGenerator, PasswordEncoder passwordEncoder) {
		this.roleService = roleService;
		this.userService = userService;
		this.authManager = authManager;
		this.tokenGenerator = tokenGenerator;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest req) {

		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenGenerator.generateToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		Long id = userDetails.getId();
		String username = userDetails.getUsername();
		List<String> roles = userDetails.getRoles();
		List<String> authorities = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		// TODO:
		Date d = new Date(System.currentTimeMillis());
		System.out.println(d);

		return ResponseEntity.ok(new JwtResponse(jwt, id, username, roles, authorities, d));
	}

	@PostMapping("register")
	public ResponseEntity<?> registerNewUser(@Valid @RequestBody LoginRequest req) {
		// TODO: guards

		AuthUser user = new AuthUser(req.getUsername(), passwordEncoder.encode(req.getPassword()));

		List<AuthRole> roles = new ArrayList<>();
		roles.add(roleService.getByName("ROLE_USER"));

		user.setRoles(roles);
		userService.save(user);

		return ResponseEntity.ok(new JsonResponse("ok", user));
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
		return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
	}

}
