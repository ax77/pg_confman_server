package ars.srv.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "auth_user")
public class AuthUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "auth_user_id")
	private Long id;

	@Column(name = "auth_user_name")
	@Size(max = 255)
	private String name;

	@Column(name = "auth_user_pw")
	@Size(max = 255)
	private String password;

	@ManyToMany
	@JoinTable(name = "auth_user_has_roles", joinColumns = {
			@JoinColumn(name = "auth_user_id") }, inverseJoinColumns = { @JoinColumn(name = "auth_role_id") })
	private List<AuthRole> roles;

	public AuthUser() {
	}

	public AuthUser(Long id, @Size(max = 255) String name, @Size(max = 255) String password, List<AuthRole> roles) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AuthRole> getRoles() {
		return roles;
	}

	public void setRoles(List<AuthRole> roles) {
		this.roles = roles;
	}

}
