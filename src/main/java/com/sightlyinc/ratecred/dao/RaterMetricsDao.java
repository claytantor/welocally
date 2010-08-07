package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.RaterMetrics;

public interface RaterMetricsDao {
	
	public RaterMetrics findByRater(Rater t);

}
