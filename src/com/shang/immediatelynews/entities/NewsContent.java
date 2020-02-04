package com.shang.immediatelynews.entities;

import java.io.Serializable;

public class NewsContent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String authorImageUrl;
	private String authorName;
	private String authorType;
	private String content;
	private boolean isPhraised;
	private boolean isCollected;
	
	public NewsContent() {
		super();
	}

	public NewsContent(String title, String authorImageUrl, String authorName, String content) {
		super();
		this.title = title;
		this.authorImageUrl = authorImageUrl;
		this.authorName = authorName;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthorImageUrl() {
		return authorImageUrl;
	}

	public void setAuthorImageUrl(String authorImageUrl) {
		this.authorImageUrl = authorImageUrl;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "NewsContent [title=" + title + ", authorImageUrl=" + authorImageUrl + ", authorName=" + authorName
				+ ", authorType=" + authorType + ", content=" + content + ", isPhraised=" + isPhraised
				+ ", isCollected=" + isCollected + "]";
	}
}
