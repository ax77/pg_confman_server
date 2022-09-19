package com.pc_builder.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "auth_user")
@Data
public class AuthUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_user_id")
	private Long id;

	@Column(name = "auth_user_name", nullable = false)
	@Size(max = 255)
	private String name;

	@Column(name = "auth_user_pw", nullable = false)
	@Size(max = 255)
	private String password;

	//@formatter:off
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(name = "auth_user_has_roles"
		, joinColumns 	     = { @JoinColumn(name = "auth_user_id") }
		, inverseJoinColumns = { @JoinColumn(name = "auth_role_id") }
		, foreignKey 		 = @ForeignKey(name = "fk_auth_user_id")
		, inverseForeignKey  = @ForeignKey(name = "fk_auth_role_id")
	)
	private List<AuthRole> roles;
	//@formatter:on

}
