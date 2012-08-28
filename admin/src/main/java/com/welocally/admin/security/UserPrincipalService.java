package com.welocally.admin.security;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import org.scribe.model.Token;

import com.noi.utility.spring.service.BLServiceException;

public interface UserPrincipalService {
	public Long saveUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException;
	public void deleteUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException;
	public UserPrincipal createUserPrincipal();
	public boolean authenticate(String username, String password) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUser(String username) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUserByAuthId(String authId) throws UserPrincipalServiceException,UserNotFoundException;
	public UserPrincipal loadUserEmail(String authId) throws UserPrincipalServiceException,UserNotFoundException;
	
	public UserPrincipal findByUserName(String username) throws UserPrincipalServiceException;
	
	public void saveUserPrincipalRoles(UserPrincipal up, Set<Role> roles) throws UserPrincipalServiceException;
	public void saveUserPrincipalRoles(UserPrincipal up, List<String> roles) throws BLServiceException;
	
	
	public void deleteUserPrincipalRole(UserPrincipal up, Role role) throws UserPrincipalServiceException;
	public void deleteUserPrincipalRoles(UserPrincipal up) throws UserPrincipalServiceException;
	
	public List<UserPrincipal>  findByUserNameLike(String username);
	
	//move this back to services
	public UserPrincipal findUserByTwitterScreenName(String twitterScreenName) throws BLServiceException;
	public List<UserPrincipal> findUsersByTwitterIds(Long[] twiiterids) throws BLServiceException;
	public List<UserPrincipal> findAll() throws BLServiceException;
	public UserPrincipal findUserByPrimaryKey(Long id) throws BLServiceException;
	
	
	//roles
	public List<Role> findAllRoles() throws UserPrincipalServiceException;
	public Role findRole(String name) throws UserPrincipalServiceException;
	
	public Boolean hasUserRole(UserPrincipal up, String roleName) throws UserPrincipalServiceException;

    void signUp(UserPrincipal user, List<String> roleNames) throws UserPrincipalServiceException;
    
    void activateWithRoles(UserPrincipal entity, List<String> roleNames) throws UserPrincipalServiceException;
    void addRoles(UserPrincipal entity, List<String> roleNames) throws UserPrincipalServiceException;
    void removeRoles(UserPrincipal entity, List<String> roleNames) throws UserPrincipalServiceException;
    
    
    void deactivate(UserPrincipal entity) throws UserPrincipalServiceException;

    UserPrincipal findUserByEmail(String email);
    
    /*
     *  private String key;   
    private String authUrl;
    private Token authToken;
    private Token accessToken;
     */
    public void saveAuthInfo(
            UserPrincipal entity, 
            String key, 
            String authUrl, 
            String authToken,
            String authSecret,
            String accessToken,
            String accessSecret
            ) throws UserPrincipalServiceException;
    
    
    public String makeMD5Hash(String unhashed) throws NoSuchAlgorithmException;
    public void updateAllPasswordsToMD5();
}
