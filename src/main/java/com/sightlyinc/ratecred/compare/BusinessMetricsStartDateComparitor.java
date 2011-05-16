package com.sightlyinc.ratecred.compare;

import java.util.Comparator;

import com.sightlyinc.ratecred.model.BusinessMetrics;

public class BusinessMetricsStartDateComparitor implements Comparator<BusinessMetrics> {

	@Override
	public int compare(BusinessMetrics o1, BusinessMetrics o2) {
		return o1.getStartTime().compareTo(o2.getStartTime());
	}

}
