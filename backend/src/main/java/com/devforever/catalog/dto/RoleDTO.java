package com.devforever.catalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.devforever.catalog.entities.Role;

public class RoleDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	private String authority;
	
	private Set<UserDTO> users = new HashSet<>();
	
	public RoleDTO() {
		
	}

	public RoleDTO(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}
	
	public RoleDTO(Role entity) {
		id = entity.getId();
		authority = entity.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Set<UserDTO> getUsers() {
		return users;
	}	
}
