package org.uva.predictions;

import java.util.List;

public class Observation {
	private boolean finished;
	private Form form;
	private List<Question> questions;
	private Question newest;
	private int noQuestions;
	
	public Observation(boolean isFinished, Form form, Iterable<Question> questions) {
		this.finished = isFinished;
		this.form = form;
		
		for(Question q : questions) {
			newest = q;
			noQuestions++;
		}
	}
	
	public boolean isFinished() {
		return finished;
	}
	public Form getForm() {
		return form;
	}
	public Iterable<Question> getQuestions() {
		return questions;
	}
	public int getNoQuestions() {
		return noQuestions;
	}
	public Question getLastAsked() {
		return newest;
	}
}
