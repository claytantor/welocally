package com.sightlyinc.ratecred.authentication;

import java.util.List;
import java.util.Set;

import com.noi.utility.spring.service.BLServiceException;

public interface UserPrincipalService {
	public Long saveUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException;
	public void deleteUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException;
	public UserPrincipal createUserPrincipal();
	public boolean authenticate(String username, String password) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUser(String username) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUserByAuthId(String authId) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUserEmail(String authId) throws UserPrincipalServiceException,UserNotFoundException;
	
	public void saveUserPrincipalRoles(UserPrincipal up, Set<Role> roles) throws UserPrincipalServiceException;
	public void deleteUserPrincipalRole(UserPrincipal up, Role role) throws UserPrincipalServiceException;
	
	//move this back to services
	public UserPrincipal findUserByTwitterScreenName(String twitterScreenName) throws BLServiceException;
	public List<UserPrincipal> findUsersByTwitterIds(Long[] twiiterids) throws BLServiceException;
	public List<UserPrincipal> findAll() throws BLServiceException;
	public UserPrincipal findUserByPrimaryKey(Long id) throws BLServiceException;
	
	
	//roles
	public List<Role> findAllRoles() throws UserPrincipalServiceException;
	
	//public Long saveUserRole(Role r) throws UserPrincipalServiceException;
	
}
