package com.welocally.admin.security;

import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.spring.service.BLServiceException;
import com.welocally.admin.util.DigestUtils;


@Service("userPrincipalService")
@Transactional(readOnly = false)
public class UserPrincipalServiceImpl implements UserDetailsService, UserPrincipalService, AuthenticationProvider {

	@Value("${authenticationProvider.useMD5Hash:true}")
	private Boolean useMD5Hash;
	
    @Autowired
	private UserPrincipalDao userPrincipalDao;
    
    @Autowired
	private RoleDao roleDao;
    
    @Autowired DigestUtils digestUtils;
	
	static Logger logger = Logger.getLogger(UserPrincipalServiceImpl.class);
		
	@Override
	public Role findRole(String name) throws UserPrincipalServiceException {
		return roleDao.findByName(name);
	}
			
	@Override
	public String makeMD5Hash(String unhashed) throws NoSuchAlgorithmException {	    
	    return digestUtils.makeMD5Hash(unhashed);
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
    public void removeRoles(UserPrincipal entity, List<String> roleNamesToRemove)
            throws UserPrincipalServiceException {
	    
	    Set<String> roleNamesOrig = new HashSet<String>();
        Set<Role> roles = entity.getRoles();
        for (Role role : roles) {
            
            //only add for save if not in list 
            if(!roleNamesToRemove.contains(role.getRole())){
                roleNamesOrig.add(role.getRole());
            }          
        }
        
        try {
            saveUserPrincipalRoles(entity, new ArrayList<String>(roleNamesOrig));
        } catch (BLServiceException e) {
            throw new UserPrincipalServiceException("problem saving roles",e);
        }  
        
    }



    @Override
    public void addRoles(UserPrincipal entity, List<String> roleNames)
            throws UserPrincipalServiceException {
	   
	    
	    Set<String> roleNamesOrig = new HashSet<String>();
	    Set<Role> roles = entity.getRoles();
	    for (Role role : roles) {
	        roleNamesOrig.add(role.getRole());
	    }
	    
	    for (String string : roleNames) {
	        roleNamesOrig.add(string);
        }
	    
	    try {
            saveUserPrincipalRoles(entity, new ArrayList<String>(roleNamesOrig));
        } catch (BLServiceException e) {
            throw new UserPrincipalServiceException("problem saving roles",e);
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
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails details = null;
		UserPrincipal principal = 
			getUserPrincipalDao().findByUserName(username);
		
		if(principal==null ){
		    String[] sa = {"ROLE_USER"};
	        List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
	        for (int i = 0; i < sa.length; i++) {
	            GrantedAuthority ga = new GrantedAuthorityImpl(sa[i]);
	            authoritiesList.add(ga);
	        }  
	        
	        User u = new User(username,"",true,true,true,true,authoritiesList);
	        UserPrincipal up = new UserPrincipal();
	        up.setAuthenticated(true);
	        up.setEnabled(true);
	        up.setLocked(false);
	        up.setCredentialsExpired(false);
	        up.setUsername(username);
	        this.userPrincipalDao.save(up);
	        Set<Role> roles = new HashSet<Role>();
	        Role r = new Role();
	        r.setRole("ROLE_USER");
	        r.setRoleGroup("ROLE_USER");
	        r.setUser(up);
	        roles.add(r);
	        up.setRoles(roles);
	        
	        //up.set
	        this.userPrincipalDao.save(up);
	        
	        
	        return u;
		}
		else
		{
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

    @Override
    /**
     * this is really bad that we have to do this but I cant figure 
     * out why it wont transact. its stupid really, there us no good reason
     * I tried a ton of different propagation types. pissed off really. 1 hour
     * wasted. 
     */
    public void saveAuthInfo(
            UserPrincipal entity, 
            String key, 
            String authUrl, 
            String authToken,
            String authSecret,
            String accessToken,
            String accessSecret
            ) throws UserPrincipalServiceException {
        
        UserPrincipal bound = 
            userPrincipalDao.findByUserName(entity.getUsername());
        bound.setAccessToken(accessToken);
        bound.setAccessSecret(accessSecret);
        bound.setAuthToken(authToken);
        bound.setAuthSecret(authSecret);
        bound.setAuthKey(key);
        bound.setAuthUrl(authUrl);
        //userPrincipalDao.saveWithCommit(bound);
        userPrincipalDao.save(bound);
    }




	

}
