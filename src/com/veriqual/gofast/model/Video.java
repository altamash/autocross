package com.veriqual.gofast.model;

public class Video {
	public static String FIRSTVIDEO = "First Video";
	public static String SECONDVIDEO = "Second Video";
	private String order;
	private String url;
	private Tagging tagging;
	private int startOffset;
	
	public int getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
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
