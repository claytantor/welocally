package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;

import org.mcavallo.opencloud.Tag;

public class TagScoreComparitor implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		Double s1 = new Double(o1.getScore());
		Double s2 = new Double(o2.getScore());
		
		return s2.compareTo(s1);
	}

}
