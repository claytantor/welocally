package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;
import java.util.Map;

public class DescendingRateMapStringComparitor implements Comparator<String>{

	@Override
	public int compare(String o1, String o2) {
		String[] parts1 = o1.split(",");
		String[] parts2 = o2.split(",");
		return parts2[0].compareTo(parts1[0]);
	}

}
