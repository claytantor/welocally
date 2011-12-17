package com.sightlyinc.ratecred.authentication;

import javax.persistence.*;

@Entity
@Table(name="role")
public class Role {
    //	id for entity
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private Long id;


	//properties
    @Column(name = "role_group")
	private java.lang.String roleGroup;	
	private java.lang.String role;	
	private java.lang.Integer version = new Integer(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="principal_id")
    private UserPrincipal user;


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


    public UserPrincipal getUser() {
        return user;
    }

    public void setUser(UserPrincipal user) {
        this.user = user;
    }
}
