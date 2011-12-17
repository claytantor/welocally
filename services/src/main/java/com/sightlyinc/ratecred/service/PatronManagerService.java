package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PatronMetrics;
import com.sightlyinc.ratecred.model.PlaceCityState;

/**
 * ok this interface currently lives in the app tier because it bridges the user from
 * the oauth infrastructure into our user principal which is our authourity infrastructure
 * that resides in the service module 
 * 
 * @author claygraham
 *
 */
public interface PatronManagerService {
	
	
	public Patron savePatronByUser(UserPrincipal u) throws BLServiceException;	
	public void saveUpdatePatronByUser(UserPrincipal u, Patron r) throws BLServiceException;
	public void saveUpdatePatronByUsername(String username) throws BLServiceException;
	
	//patron functions
	public Patron findPatronByPrimaryKey(Long string) throws BLServiceException;			
	public List<Patron> findPatronByPrimaryKeys(final Long[] ids) throws BLServiceException;
	public List<Patron> findPatronByStatus(String status) throws BLServiceException;
	
	public void saveConvertPatron(Patron fromRater, Patron toRater) throws BLServiceException;

	
	public Patron findPatronByUsername(String userName) throws BLServiceException;
	//public Rater findRaterByTwitterScreenName(String twitterScreenName) throws BLServiceException;
	
	public Patron findPatronByAuthId(String authId) throws BLServiceException;	
	
	//create anonymous rater, must be saved
	//public Patron createAnonymousRater();
	
	public List<Patron> findAllPatrons() throws BLServiceException;	
	
	public List<Patron> findPatronsRatedSince(Long millis) throws BLServiceException;
	
	public List<Patron> findPatronsByScreenNames(String[] screenNames) throws BLServiceException;
				
	public void deletePatron(Patron entity) throws BLServiceException;
	
	public void savePatron(Patron entity) throws BLServiceException;
	
	
			
	public List<Patron> findPatronsByScoreDesc(int size) throws BLServiceException;
	
	public List<Patron> findPatronsByCityStateScoreDesc(PlaceCityState cs, int size) throws BLServiceException;
		
	//rater metrics
	public PatronMetrics findMetricsByPatron(Patron t);
	

	

}
