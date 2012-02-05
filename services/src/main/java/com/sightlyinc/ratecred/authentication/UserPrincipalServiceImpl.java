package com.sightlyinc.ratecred.authentication;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Publisher;

@Service("userPrincipalService")
@Transactional(readOnly = true)
public class UserPrincipalServiceImpl implements UserDetailsService, UserPrincipalService, AuthenticationProvider {

	@Value("${authenticationProvider.useMD5Hash:true}")
	private Boolean useMD5Hash;
	
    @Autowired
	private UserPrincipalDao userPrincipalDao;
    
    @Autowired
	private RoleDao roleDao;
	
	static Logger logger = Logger.getLogger(UserPrincipalServiceImpl.class);
	
	
	@Override
	public Role findRole(String name) throws UserPrincipalServiceException {
		return roleDao.findByName(name);
	}
	
	
	
	@Override
	public String makeMD5Hash(String unhashed) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
		messageDigest.update(unhashed.getBytes(),0, unhashed.length());  
		String hashedPass = new BigInteger(1,messageDigest.digest()).toString(16);  
		if (hashedPass.length() < 32) {
		   hashedPass = "0" + hashedPass; 
		}
		return hashedPass;
	}



	@Transactional(readOnly = false)
	public void updateAllPasswordsToMD5() {
		try {
			List<UserPrincipal> up = findAll();
			for (UserPrincipal userPrincipal : up) {
				String hashedPass = makeMD5Hash(userPrincipal.getPassword());
				System.out.println("UPDATE `user_principal` SET `password`='"+hashedPass+"' WHERE `user_name`='"+userPrincipal.getUsername()+"'");
				userPrincipal.setPassword(hashedPass);
				
				saveUserPrincipal(userPrincipal);				
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error("no md5 hash", e);
		} catch (BLServiceException e) {
			logger.error("problem getting user", e);
		} catch (UserPrincipalServiceException e) {
			logger.error("problem saving user", e);
		}		
	}

	
	
	
    @Override
    @Transactional(readOnly = false)
	public void activateWithRoles(UserPrincipal entity, List<String> roleNames)
			throws UserPrincipalServiceException {
        // create a role for the user so they can log in
        try {
        	
        	entity.setCredentialsExpired(false);
        	entity.setLocked(false);
        	entity.setEnabled(true);
        	
            saveUserPrincipalRoles(entity, roleNames);

            userPrincipalDao.save(entity);
        } catch (BLServiceException e) {
            throw new UserPrincipalServiceException(e);
        }
		
	}



	@Override
	@Transactional(readOnly = false)
	public void deactivate(UserPrincipal entity)
			throws UserPrincipalServiceException {
    	entity.setCredentialsExpired(false);
    	entity.setLocked(true);
    	entity.setEnabled(true);
        userPrincipalDao.save(entity);
	}



	@Override
    @Transactional(readOnly = false)
    public void signUp(UserPrincipal entity, List<String> roleNames) throws UserPrincipalServiceException {
        UserPrincipal user = userPrincipalDao.findByEmail(entity.getEmail());
        if (user != null) {
            throw new UserPrincipalServiceException("Email address already registered");
        }
        user = userPrincipalDao.findByUserName(entity.getUsername());
        if (user != null) {
            throw new UserPrincipalServiceException("Username already in use");
        }
        // create a role for the user so they can log in
        try {
            saveUserPrincipalRoles(entity, roleNames);

            userPrincipalDao.save(entity);
        } catch (BLServiceException e) {
            throw new UserPrincipalServiceException(e);
        }
        // TODO generate keys, tokens, etc. for use with plugin
        // TODO email the user with their account info
    }

    @Override
    public UserPrincipal findUserByEmail(String email) {
        return userPrincipalDao.findByEmail(email);
    }

    @Override
	public List<UserPrincipal> findByUserNameLike(String username) {
		return userPrincipalDao.findByUserNameLike(username);
	}

	@Override
	public List<Role> findAllRoles() throws UserPrincipalServiceException {
		return roleDao.findAll();
	}

	@Override
	public List<UserPrincipal> findAll() throws BLServiceException {
		return userPrincipalDao.findAll();
	}
	
	

	@Override
	@Transactional(readOnly = false)
	public void saveUserPrincipalRoles(UserPrincipal up, List<String> roles)
			throws BLServiceException {
		
		try {
			//delete the old roles
			deleteUserPrincipalRoles(up);
			Set<Role> newRoles = new HashSet<Role>();			
			for (String role : roles) {
				Role r = new Role();
				r.setRole(role);
				r.setUser(up);
				r.setRoleGroup(role);
				newRoles.add(r);
			}
			if (newRoles.size() > 0)
				up.setRoles(newRoles);
			
		} catch (UserPrincipalServiceException e) {
			throw new BLServiceException(e);
		}
		
		
		
	}
	
	

	@Override
	public Boolean hasUserRole(UserPrincipal up, String roleName)
			throws UserPrincipalServiceException {
		for (Role role : up.getRoles()) {
			if(role.getRole().equals(roleName))
				return true;
			
		}
		return false;
	}



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
    @Transactional(readOnly = false)
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
			
			String checkPassword = password;
			try {
				if(useMD5Hash)
					checkPassword = makeMD5Hash(checkPassword);
			} catch (NoSuchAlgorithmException e) {
				logger.error("cannot make hash of password", e);
			}
							
			if(details.getPassword().equals(checkPassword))
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
		UserPrincipal principal = 
			getUserPrincipalDao().findByUserName(username);
		
		if(principal==null )
			throw new UsernameNotFoundException("cannot find user:"+username);

		
		return principal;
		
		
	}
	
	
	
	@Override
	public UserPrincipal findByUserName(String username)
			throws UserPrincipalServiceException {
		return getUserPrincipalDao().findByUserName(username);
	}



	


	/* (non-Javadoc)
	 * @see org.acegisecurity.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails details = null;
		UserPrincipal principal = 
			getUserPrincipalDao().findByUserName(username);
		
		if(principal==null )
			throw new UsernameNotFoundException("cannot find user:"+username);
		else
		{
			//UserPrincipal user = users.get(0);
			
			Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();

	        for (Iterator<Role> iter = principal.getRoles().iterator(); iter.hasNext();) {
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
	        		principal.getCredentialsExpired(), 
	        		principal.getEnabled(), 
	        		principal.getCredentialsExpired(), 
	        		principal.getLocked(), 
	        		principal.getPassword(), 
	        		principal.getUsername());
			
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
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
	
	

	@Override
	@Transactional(readOnly = false)
	public void deleteUserPrincipalRoles(UserPrincipal up)
			throws UserPrincipalServiceException {
			
		for (Role role : up.getRoles()) {
			roleDao.delete(role);
		}
		
		up.setRoles(null);
		userPrincipalDao.save(up);
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

	@Override
	public UserPrincipal findUserByPrimaryKey(Long id)
			throws BLServiceException {
		return userPrincipalDao.findByPrimaryKey(id);
	}

	@Override
	public UserPrincipal findUserByTwitterScreenName(String twitterScreenName)
			throws BLServiceException {
		return null;
	}

	@Override
	public List<UserPrincipal> findUsersByTwitterIds(Long[] twiiterids)
			throws BLServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
