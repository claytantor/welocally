package com.sightlyinc.ratecred.authentication;

import java.security.ProviderException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;


public class PrincipalBasedUserDetailsService implements UserDetailsService, UserPrincipalService, AuthenticationProvider {

	private UserPrincipalDao userPrincipalDao;
	private RoleDao roleDao;
	
	static Logger logger = Logger.getLogger(PrincipalBasedUserDetailsService.class);
	
	
	@Override
	public UserPrincipal loadUserEmail(String email)
			throws UserPrincipalServiceException, UserNotFoundException {
		UserPrincipal principal = getUserPrincipalDao().findByEmail(email);
		if(principal == null)
			throw new UserNotFoundException("The email addr:"+email+
                	" was not found in our database.");
		
		return principal;
	}

	@Override
	public void deleteUserPrincipal(UserPrincipal up)
			throws UserPrincipalServiceException {
		userPrincipalDao.delete(up);
	}

	@Override
	public boolean authenticate(String username, String password) 
			throws UserPrincipalServiceException,UserNotFoundException {
		

		try {
			UserDetails details =
				loadUserByUsername(username);
			
			if(details.getPassword().equals(password))
				return true;
			
		} catch (DataAccessException e) {
			throw new UserPrincipalServiceException(e);
		} catch (UsernameNotFoundException e) {
			throw new UserNotFoundException(e);
		}
		
		return false;
		
		
	}

	//this is for the UserPrincipalService interface
	public UserPrincipal loadUser(String username) 
	throws UserPrincipalServiceException,UserNotFoundException
	{
		UserPrincipal principal =null;
		List<UserPrincipal> users = 
			getUserPrincipalDao().findByUserName(username);
		
		if(users==null || users.size()==0)
			throw new UsernameNotFoundException("cannot find user:"+username);
		else
		{
			principal = users.get(0);
		}		
		
		return principal;
		
		
	}
	


	/* (non-Javadoc)
	 * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		
		UserDetails details = null;
		List<UserPrincipal> users = 
			getUserPrincipalDao().findByUserName(username);
		
		if(users==null || users.size()==0)
			throw new UsernameNotFoundException("cannot find user:"+username);
		else
		{
			UserPrincipal user = users.get(0);
			
			Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();

	        for (Iterator<Role> iter = user.getRoles().iterator(); iter.hasNext();) {
	          Role role = iter.next();
	          auths.add(new GrantedAuthorityImpl(role.getRole()));
	        }

	        if (auths.size() == 0) {
	          throw new UsernameNotFoundException("User has no GrantedAuthority");
	        }
	        
	        //Object[] objectArray = auths.toArray();
	        GrantedAuthority[] authArray = (GrantedAuthority[])auths.toArray(new GrantedAuthority[auths.size()]);
	        details = new DefaultUserDetails(
	        		authArray,
	    			user.getCredentialsExpired(), 
	    			user.getEnabled(), 
	    			user.getExpired(), 
	    			user.getLocked(), 
	    			user.getPassword(), 
	    			user.getUsername());
			
		}
		
		return details;
	}
	
	@Override
	public UserPrincipal loadUserByAuthId(String authId) throws UserNotFoundException, DataAccessException {
		

		UserPrincipal user = 
			getUserPrincipalDao().findByAuthId(authId);
		
		if(user==null)
			throw new UsernameNotFoundException("cannot find user with auth id:"+authId);
		
		return user;
	}

	@Override
	public Authentication authenticate(Authentication arg0)
			throws AuthenticationException {
		try {
			boolean authenticated =
				authenticate(
						arg0.getPrincipal().toString(), 
						arg0.getCredentials().toString());
			
			//if authenticated load the principal
			if(authenticated)	
			{
				Authentication auth = loadUser(arg0.getPrincipal().toString());
				auth.setAuthenticated(true);
				return auth;
			}
			else
				throw new BadCredentialsException("Username/Password does not match for " + arg0.getPrincipal().toString());			
			
		} catch (UserPrincipalServiceException e) {
			logger.error("cannot access authentication provider");
			throw new ProviderException("cannot access authentication provider");
		} catch (UserNotFoundException e) {
			logger.error("principal was not found:"+arg0.getPrincipal().toString());
			throw new BadCredentialsException("cannot access authentication provider");
		}
		
	}

	@Override
	public boolean supports(Class clazz) {
		return clazz.equals(UserPrincipal.class);
	}

	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalService#createUserPrincipal()
	 */
	public UserPrincipal createUserPrincipal() {
		return new UserPrincipal();
	}



	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalService#saveUserPrincipal(com.noi.utility.spring.UserPrincipal)
	 */
	public Long saveUserPrincipal(UserPrincipal up) throws UserPrincipalServiceException {
		//roles
		Set<Role> roles = up.getRoles();
		for (Role role : roles) {
			roleDao.save(role);
		}
		
		userPrincipalDao.save(up);
		return up.getId();
	}


	/**
	 * this will update the user principal roles to new ones
	 * 
	 */
	@Override
	public void saveUserPrincipalRoles(UserPrincipal up, Set<Role> roles)
			throws UserPrincipalServiceException {
		
		//remove the previous roles
		Set<Role> oldRoles = up.getRoles();
		for (Role role : oldRoles) {
			roleDao.delete(role);
		}
		
		//set the new ones
		up.setRoles(roles);
		
		//save
		saveUserPrincipal(up);
		

	}

	@Override
	public void deleteUserPrincipalRole(UserPrincipal up, Role role)
			throws UserPrincipalServiceException {
		if(role.getId() != null)
		{
			//Role rDel = roleDao.findByPrimaryKey(role.getId());
			up.getRoles().remove(role);
			userPrincipalDao.save(up);
			roleDao.delete(role);
		}
		
	}
	
	

	/**
	 * @return Returns the userPrincipalDao.
	 */
	public UserPrincipalDao getUserPrincipalDao() {
		return userPrincipalDao;
	}

	/**
	 * @param userPrincipalDao The userPrincipalDao to set.
	 */
	public void setUserPrincipalDao(UserPrincipalDao userPrincipalDao) {
		this.userPrincipalDao = userPrincipalDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	

}
