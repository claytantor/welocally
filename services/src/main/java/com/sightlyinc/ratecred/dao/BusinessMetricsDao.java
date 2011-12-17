package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;

public interface BusinessMetricsDao extends BaseDao<BusinessMetrics> {
	
	public Long findLastBusinessLocationMetricsTime( BusinessLocation bl);
	
	public List<BusinessMetrics> findDailyByBusinessLocationDateRange(
			final BusinessLocation bl, final Date startDate, final Date endDate);
	
	public BusinessMetrics findBusinessMetricsByLocationAndStartTime(
			BusinessLocation bl, Long startTime);
	
	public Long mineFirstBusinessMetricsTime(BusinessLocation bl);
	public Long mineLastBusinessMetricsTime(BusinessLocation bl);
	public BusinessMetrics mineMetricsForDateRange(BusinessLocation bl,  Date startDate,  Date endDate);

}
