package com.pc_builder.entity.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "auth_role_resource_privilege")
@Data
public class AuthRoleResourcePrivilege {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_role_resource_privilege_id")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_role_id", referencedColumnName = "auth_role_id", foreignKey = @ForeignKey(name = "fk_auth_role_id"), nullable = false)
	private AuthRole role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_resource_id", referencedColumnName = "auth_resource_id", foreignKey = @ForeignKey(name = "fk_auth_resource_id"), nullable = false)
	private AuthResource resource;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "auth_privilege_id", referencedColumnName = "auth_privilege_id", foreignKey = @ForeignKey(name = "fk_auth_privilege_id"), nullable = false)
	private AuthPrivilege privilege;

}
