package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;
import java.util.Map;

public class DescendingComparitor implements Comparator<Map.Entry>{

	@Override
	public int compare(Map.Entry o1, Map.Entry o2) {
		return ((Comparable) ((Map.Entry) (o1)).getValue())
		.compareTo(((Map.Entry) (o2)).getValue());
	}

}
