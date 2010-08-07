package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Rater;

public interface ComplimentDao {

	public Compliment findByPrimaryKey(Long id);	
	public List<Compliment> findAll();
	public void save(Compliment entity);
	public void delete(Compliment entity);
	public List<Compliment> findByRaterBetweenTimes(Rater towards, Date startTime, Date endTime);
	public Long findCountByRaterBetweenTimes(Rater towards, Date startTime, Date endTime);
	
}
