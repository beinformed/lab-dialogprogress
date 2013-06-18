package com.beinformed.research.labs.dialogprogress;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an answered question on a form.
 * 
 */
public class Question {
	private String id;
	private String answer;
	private String type;
	private long timestamp;
	
	/**
	 * 
	 * @param id
	 * Some unique id that separates the question from other questions, but is
	 * constant between observations (if the timestamp changes, this should remain the same).
	 * @param answer
	 * The answer given by the user.
	 * @param timestamp
	 * The epoch timestamp of the moment this question was answered.
	 */
	public Question(String id, String answer, long timestamp, String type) {
		this.id = id;
		this.answer = answer;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	/**
	 * Return an id that identifies the question on a specific form.
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 * Returns the answer given by the user.
	 * @return
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * Returns the time in seconds since the start of the observation.
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	public String toString() {
		String date = SimpleDateFormat.getDateTimeInstance().format(new Date(timestamp));
		return  date + ": " + id + "=" + answer + "-" + type;
	}
}












