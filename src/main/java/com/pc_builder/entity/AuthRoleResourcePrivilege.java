package com.pc_builder.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "auth_role_resource_privilege")
public class AuthRoleResourcePrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "auth_role_resource_privilege_id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_role_id")
	private AuthRole role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_resource_id")
	private AuthResource resource;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_privilege_id")
	private AuthPrivilege privilege;

	public AuthRoleResourcePrivilege() {
	}

	public AuthRoleResourcePrivilege(Long id, AuthRole role, AuthResource resource, AuthPrivilege privilege) {
		this.id = id;
		this.role = role;
		this.resource = resource;
		this.privilege = privilege;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuthRole getRole() {
		return role;
	}

	public void setRole(AuthRole role) {
		this.role = role;
	}

	public AuthResource getResource() {
		return resource;
	}

	public void setResource(AuthResource resource) {
		this.resource = resource;
	}

	public AuthPrivilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(AuthPrivilege privilege) {
		this.privilege = privilege;
	}

}
