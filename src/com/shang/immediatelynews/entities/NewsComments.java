package com.shang.immediatelynews.entities;

import java.io.Serializable;

public class NewsComments implements Serializable {

	private static final long serialVersionUID = 1L;
	private String newsUserImageUrl;
	private String comment;
	private String username;
	
	public NewsComments() {
		super();
	}
	public NewsComments(String newsUserImageUrl, String comment, String username) {
		super();
		this.newsUserImageUrl = newsUserImageUrl;
		this.comment = comment;
		this.username = username;
	}
	public String getNewsUserImageUrl() {
		return newsUserImageUrl;
	}
	public void setNewsUserImageUrl(String newsUserImageUrl) {
		this.newsUserImageUrl = newsUserImageUrl;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "VideoComments [videoUserImageUrl=" + newsUserImageUrl + ", comment=" + comment + ", username="
				+ username + "]";
	}
}
