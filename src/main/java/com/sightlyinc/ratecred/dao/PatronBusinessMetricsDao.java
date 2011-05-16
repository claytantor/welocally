package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.PatronBusinessMetrics;

public interface PatronBusinessMetricsDao {
	
	public List<PatronBusinessMetrics> mineBusinessLocationMetricsForDateRange(
			BusinessLocation bl,  
			Date startDate,  
			Date endDate);
	
	public List<PatronBusinessMetrics> mineBusinessLocationMetricsForRaters(
			BusinessLocation bl,  
			Long[] raterIds,
			Date startDate, 
			Date endDate);
	

	public List<PatronBusinessMetrics> mineBusinessMetricsForDateRange(
			Business b,  
			Date startDate,  
			Date endDate);
	
	public List<PatronBusinessMetrics> mineBusinessMetricsForRaters(
			Business b,  
			Long[] raterIds,
			Date startDate, 
			Date endDate);


}
