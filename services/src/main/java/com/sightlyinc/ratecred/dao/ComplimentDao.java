package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Patron;

public interface ComplimentDao extends BaseDao<Compliment> {
	public List<Compliment> findByRaterBetweenTimes(Patron towards,
			Date startTime, Date endTime);
	public Long findCountByRaterBetweenTimes(Patron towards, Date startTime,
			Date endTime);
}
