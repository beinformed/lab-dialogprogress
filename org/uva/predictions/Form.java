package org.uva.predictions;

public class Form {
	private int id;
	private int noQuestions;
	
	public Form(int id, int noQuestions) {
		this.id = id;
		this.noQuestions = noQuestions;
	}
	
	public int getId() {
		return id;
	}
	public int getNoQuestions() {
		return noQuestions;
	}
}
