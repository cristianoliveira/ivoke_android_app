package com.app.ivoke.objects;

public class ConvesationMessage {

	private int id;
	private int conversation_id;
	private int user_id;
	private String message;
	private int time;
	private int status;
	
	int getId() {
		return id;
	}
	void setId(int id) {
		this.id = id;
	}
	int getConversationId() {
		return conversation_id;
	}
	void setConversationId(int conversation_id) {
		this.conversation_id = conversation_id;
	}
	public int getUserId() {
		return user_id;
	}
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
