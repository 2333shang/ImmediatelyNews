package com.shang.immediatelynews.entities;

import java.io.Serializable;

public class NewsCommentReplies implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String reply;
	
	public NewsCommentReplies() {
		super();
	}
	public NewsCommentReplies(String reply, String username) {
		super();
		this.reply = reply;
		this.username = username;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
