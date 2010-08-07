package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;
import com.sightlyinc.ratecred.model.RaterBusinessMetrics;

public interface RaterBusinessMetricsDao {
	
	public List<RaterBusinessMetrics> mineBusinessLocationMetricsForDateRange(
			BusinessLocation bl,  
			Date startDate,  
			Date endDate);
	
	public List<RaterBusinessMetrics> mineBusinessLocationMetricsForRaters(
			BusinessLocation bl,  
			Long[] raterIds,
			Date startDate, 
			Date endDate);
	

	public List<RaterBusinessMetrics> mineBusinessMetricsForDateRange(
			Business b,  
			Date startDate,  
			Date endDate);
	
	public List<RaterBusinessMetrics> mineBusinessMetricsForRaters(
			Business b,  
			Long[] raterIds,
			Date startDate, 
			Date endDate);


}
