package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PatronMetrics;

public interface PatronMetricsDao {
	
	public PatronMetrics findByRater(Patron t);

}
