package com.veriqual.gofast.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class ComparisonsList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ComparisonsList comparisonsList;
	Set<Comparison> comparisons = new TreeSet<Comparison>();

	public static ComparisonsList getInstance() {
		if (comparisonsList == null) {
			comparisonsList = new ComparisonsList();
		}
		return comparisonsList;
	}

	public Set<Comparison> getComparisons() {
		return comparisons;
	}

	public void setComparisons(Set<Comparison> comparisons) {
		this.comparisons = comparisons;
	}

}