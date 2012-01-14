package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.admin.model.PlaceRatingEvaluator;
import com.sightlyinc.ratecred.model.Award;

/**
 * test 2
 * @author claygraham
 *
 */
public class DescendingAwardDateComparitor implements Comparator<Award>{
	
	static Logger logger = Logger.getLogger(DescendingAwardDateComparitor.class);

	@Override
	public int compare(Award o1, Award o2) {
		//logger.debug("o1:"+o1.getOffer().getExpireDateMillis()+" o2:"+o2.getOffer().getExpireDateMillis());
		return o1.getTimeCreated().compareTo(o2.getTimeCreated());
	}

}
