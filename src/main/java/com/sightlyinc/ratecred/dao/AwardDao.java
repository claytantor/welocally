package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;

public interface AwardDao {
	
	public Award findByPrimaryKey(Long id);	
	public Award findByKeyname(String kn);
	
	public List<Award> findByOwnerBetweenTimes(Rater towards, Date startTime, Date endTime);
	public List<Award> findByOwner(Rater towards);
	public List<Award> findByOwnerAwardType(Rater towards, AwardType at);
	public List<Award> findByOwnerTypePlaceCityState(Rater towards, AwardType at, PlaceCityState pcs);
	
	public Long findCountByOwnerBetweenTimes(final Rater towards,
			final Date startTime, final Date endTime);
	
	public List<Award> findByBusiness(Business b);
	
	public void delete(Award entity);	
	public void save(Award entity);
	public List<Award> findAll();		
	

}
