package com.veriqual.gofast.model;

import java.util.Date;

public class Comparison implements Comparable<Date> {
	Video firstVideo;
	Video secondVideo;
	Date createdAt;

	public Comparison() {
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

	@Override
	public int compareTo(Date another) {
		// TODO Auto-generated method stub
		return 0;
	}
}
