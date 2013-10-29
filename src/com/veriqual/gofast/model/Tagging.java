package com.veriqual.gofast.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Tagging {
	public static String STARTTAG = "Start Tag";
	public static String FINISHTAG = "Finish Tag";
	private Map<String, Integer> tags = new LinkedHashMap<String, Integer>();
	
	public void addTag(String tag, Integer val) {
		tags.put(tag, val);
	}
	
	public boolean containsStartTag() {
		return tags.get(STARTTAG) != null;		
	}
	
	public boolean containsEndTag() {
		return tags.get(FINISHTAG) != null;		
	}
	
	public Map<String, Integer> getTags() {
		return tags;
	}
}
