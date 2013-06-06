package org.uva.predictions;

/**
 * Represents an answered question on a form.
 * 
 */
public class Question {
	private int id;
	private String answer;
	private long timestamp;
	
	/**
	 * 
	 * @param id
	 * Some unique id that separates the question from other questions, but is
	 * constant between observations (if the timestamp changes, this should remain the same).
	 * @param answer
	 * The answer given by the user.
	 * @param timestamp
	 * The time in seconds since the start of the observation.
	 */
	public Question(int id, String answer, long timestamp) {
		this.id = id;
		this.answer = answer;
		this.timestamp = timestamp;
	}
	
	/**
	 * Return an id that identifies the question on a specific form.
	 * @return
	 */
	public int getId() {
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
}