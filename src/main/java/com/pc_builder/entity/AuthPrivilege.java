package com.pc_builder.entity;

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
@Table(name = "auth_privilege")
@Data
public class AuthPrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_privilege_id")
	private Long id;

	@Column(name = "auth_privilege_name", nullable = false)
	@Size(max = 255)
	private String name;

	@OneToMany(mappedBy = "privilege", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<AuthRoleResourcePrivilege> authRoleResourcePrivileges;
}
