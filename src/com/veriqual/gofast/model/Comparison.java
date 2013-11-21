package com.veriqual.gofast.model;

import java.io.Serializable;
import java.util.Date;

public class Comparison implements Comparable<Date>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Video firstVideo;
	Video secondVideo;
	Date createdAt;
	String name;

	public Comparison(Video first,	Video second) {
		firstVideo = first;
		secondVideo = second;
		createdAt = new Date();
	}

	public Video getFirstVideo() {
		return firstVideo;
	}

	public void setFirstVideo(Video firstVideo) {
		this.firstVideo = firstVideo;
	}

	public Video getSecondVideo() {
		return secondVideo;
	}

	public void setSecondVideo(Video secondVideo) {
		this.secondVideo = secondVideo;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Date date) {
		if (createdAt.before(date)) {
			return -1;
		} else if (createdAt.before(date)) {
			return 1;
		}
		return 0;
	}
}
