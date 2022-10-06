package com.pc_builder.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pc_builder.entity.auth.AuthRole;
import com.pc_builder.entity.auth.AuthRoleResourcePrivilege;
import com.pc_builder.entity.auth.AuthUser;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = -4176921433739844396L;

	private Long id;

	private String name;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	private List<String> roles;

	public UserDetailsImpl(Long id, String name, String password, Collection<? extends GrantedAuthority> authorities,
			List<String> roles) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.authorities = authorities;
		this.roles = roles;
	}

	public static UserDetailsImpl buildUserDetailsImplFromAuthUser(AuthUser user) {
		return new UserDetailsImpl(user.getId(), user.getName(), user.getPassword(), buildAuthorities(user),
				buildRoles(user));
	}

	private static List<String> buildRoles(AuthUser user) {
		List<String> result = new ArrayList<>();
		for (AuthRole role : user.getRoles()) {
			result.add(role.getName().toUpperCase());
		}
		return result;
	}

	private static List<GrantedAuthority> buildAuthorities(AuthUser user) {
		List<GrantedAuthority> result = new ArrayList<>();
		for (AuthRole role : user.getRoles()) {
			for (AuthRoleResourcePrivilege rrp : role.getAuthRoleResourcePrivileges()) {
				String authority = rrp.getResource().getName() + "_" + rrp.getPrivilege().getName();
				result.add(new SimpleGrantedAuthority(authority.toUpperCase()));
			}
		}
		return result;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getRoles() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // TODO:
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // TODO:
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // TODO:
	}

	@Override
	public boolean isEnabled() {
		return true; // TODO:
	}

}
