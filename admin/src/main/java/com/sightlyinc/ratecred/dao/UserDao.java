package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.User;

public interface UserDao {
	
	public User findById(Long id);
	public User findByUsername(String username);
	public User findByTwitterScreenName(String twitterScreenName);
	public List<User> findByTwitterIds(Long[] twitterIds);
	public Long save(User user);
	public void delete(User u);
	

}
