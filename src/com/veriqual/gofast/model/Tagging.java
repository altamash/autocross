package com.veriqual.gofast.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Tagging implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String STARTTAG = "Start Tag";
	public static String FINISHTAG = "Finish Tag";
	private Map<String, Long> tags = new LinkedHashMap<String, Long>();
	
	public void addTag(String tag, Long val) {
		tags.put(tag, val);
	}
	
	public boolean containsStartTag() {
		return tags.get(STARTTAG) != null;		
	}
	
	public boolean containsEndTag() {
		return tags.get(FINISHTAG) != null;		
	}
	
	public Map<String, Long> getTags() {
		return tags;
	}
}
