package org.uva.predictions;

public class Question {
	private int id;
	private String answer;
	private long timestamp;
	
	public Question(int id, String answer, long timestamp) {
		this.id = id;
		this.answer = answer;
		this.timestamp = timestamp;
	}
	
	public int getId() {
		return id;
	}
	public String getAnswer() {
		return answer;
	}
	public long getTimestamp() {
		return timestamp;
	}
}