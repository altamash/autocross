package com.veriqual.gofast.model;

import java.io.Serializable;

public class Video implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String FIRSTVIDEO = "First Video";
	public static String SECONDVIDEO = "Second Video";
	private String order;
	private String url;
	private Tagging tagging;
	private long startOffset;
	private long offset;
	
	public long getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(long startOffset) {
		this.startOffset = startOffset;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long l) {
		this.offset = l;
	}
	public Video(String number) {
		this.order = number;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Tagging getTagging() {
		return tagging;
	}
	public void setTagging(Tagging tagging) {
		this.tagging = tagging;
	}
	public String getVideoOrder() {
		return order;
	}
}
