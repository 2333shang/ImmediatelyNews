package com.shang.immediatelynews.entities;

import java.io.Serializable;

public class Videos implements Serializable {

	private static final long serialVersionUID = 1L;
	private String videoUrl;
	private String videoUserName;
	private Integer fans;
	private boolean isPhraised;
	private boolean isCollected;
	
	public Videos() {
		super();
	}

	public Videos(String videoUrl, String videoUserName, Integer fans) {
		super();
		this.videoUrl = videoUrl;
		this.videoUserName = videoUserName;
		this.fans = fans;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoUserName() {
		return videoUserName;
	}

	public void setVideoUserName(String videoUserName) {
		this.videoUserName = videoUserName;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public boolean isPhraised() {
		return isPhraised;
	}
	public void setPhraised(boolean isPhraised) {
		this.isPhraised = isPhraised;
	}
	public boolean isCollected() {
		return isCollected;
	}
	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	@Override
	public String toString() {
		return "Videos [videoUrl=" + videoUrl + ", videoUserName=" + videoUserName + ", fans=" + fans + ", isPhraised="
				+ isPhraised + ", isCollected=" + isCollected + "]";
	}
}
