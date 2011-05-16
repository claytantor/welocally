package com.sightlyinc.ratecred.authentication;

public class Role {
//	id for entity
	private Long id;


	//properties
	private java.lang.String roleGroup;	
	private java.lang.String role;	
	private java.lang.Integer version = new Integer(0);
	/**
	


	/**
	 * @return Returns the role.
	 */
	public java.lang.String getRole() {
		return role;
	}
	/**
	 * @param role The role to set.
	 */
	public void setRole(java.lang.String role) {
		this.role = role;
	}
	/**
	 * @return Returns the roleGroup.
	 */
	public java.lang.String getRoleGroup() {
		return roleGroup;
	}
	/**
	 * @param roleGroup The roleGroup to set.
	 */
	public void setRoleGroup(java.lang.String roleGroup) {
		this.roleGroup = roleGroup;
	}
	/**
	 * @return Returns the version.
	 */
	public java.lang.Integer getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(java.lang.Integer version) {
		this.version = version;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
/*	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Role)
		{
			Role other = (Role)obj;
			if(other.getRole().equals(this.role))
				return true;
			else
				return false;
		}
		else return false;
	}*/
	

}
