package com.veriqual.gofast.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ComparisonsList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ComparisonsList comparisonsList;
	ArrayList<Comparison> comparisons = new ArrayList<Comparison>();

	public static ComparisonsList getInstance() {
		if (comparisonsList == null) {
			comparisonsList = new ComparisonsList();
		}
		return comparisonsList;
	}

	public ArrayList<Comparison> getComparisons() {
		return comparisons;
	}

	public void setComparisons(ArrayList<Comparison> comparisons) {
		this.comparisons = comparisons;
	}

}
