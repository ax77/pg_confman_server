package com.pc_builder.entity.auth;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "auth_role")
@Data
public class AuthRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_role_id")
	private Long id;

	@Column(name = "auth_role_name", nullable = false)
	@Size(max = 255)
	private String name;

	@OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<AuthRoleResourcePrivilege> authRoleResourcePrivileges;

}
