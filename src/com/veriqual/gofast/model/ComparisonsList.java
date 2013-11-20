package com.veriqual.gofast.model;

import java.util.Set;
import java.util.TreeSet;

public class ComparisonsList {
	private static ComparisonsList comparisonsList;
	Set<Comparison> comparisons = new TreeSet<Comparison>();

	public static ComparisonsList getInstance() {
		if (comparisonsList == null) {
			comparisonsList = new ComparisonsList();
		}
		return comparisonsList;
	}

	public static ComparisonsList getComparisonsList() {
		return comparisonsList;
	}

	public static void setComparisonsList(ComparisonsList comparisonsList) {
		ComparisonsList.comparisonsList = comparisonsList;
	}

}
