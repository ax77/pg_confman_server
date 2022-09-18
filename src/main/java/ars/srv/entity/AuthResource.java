package ars.srv.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "auth_resource")
public class AuthResource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "auth_resource_id")
	private Long id;

	@Column(name = "auth_resource_name")
	@Size(max = 255)
	private String name;
	
	@OneToMany(mappedBy = "resource")
	private List<AuthRoleResourcePrivilege> authRoleResourcePrivileges;

	public AuthResource() {
	}

	public AuthResource(Long id, @Size(max = 255) String name) {
		this.id = id;
		this.name = name;
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

}
