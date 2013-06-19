package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a (potentially incomplete) observation of a user filling a form.
 *
 */
public class Observation {
	private boolean finished;
	private String form;
	private List<Question> questions;
	
	/**
	 * 
	 * @param isFinished 
	 * <code>true<code> if all necessary questions have been answered by the user, <code>false<code> otherwise
	 * @param form 
	 * The form the user was filling during this observation
	 * @param questions 
	 * List of the questions the user has already answered. The iterator is expected to return the questions
	 * in order, sorted by {@link Question#getTimestamp() timestamp} from early to late.
	 */
	public Observation(boolean isFinished, String form, Iterable<Question> questions) {
		this.finished = isFinished;
		this.form = form;
		this.questions = new ArrayList<Question>();
		
		for(Question q : questions) {
			this.questions.add(q);
		}
	}
	private Observation(boolean isFinished, String form, List<Question> questions) {
		this.finished = isFinished;
		this.form = form;
		this.questions = questions;
	}
	
	/**
	 * {@code true} if the user finished filling the form during this observation, {@code false} otherwise.
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}
	/**
	 * The form the user was filling during the observation.
	 * @return
	 */
	public String getForm() {
		return form;
	}
	/**
	 * All questions the user has answered during the observation.
	 * @return
	 */
	public Iterable<Question> getQuestions() {
		return questions;
	}
	/**
	 * Returns the number of questions that have been answered during the observation.
	 * @return
	 */
	public int getNoQuestions() {
		return questions.size();
	}
	/**
	 * Returns the last question the user answered.
	 * @return
	 */
	public Question getLastAsked() {
		if(questions.size() > 1)
			return questions.get(questions.size() - 1);
		else
			return questions.get(0);
	}
	public Question getFirstAsked() {
		return questions.get(0);
	}
	
	public int getLearnValue(PredictionUnit unit) {
		if(unit == PredictionUnit.Time)
			return (int) (getLastAsked().getTimestamp() - getFirstAsked().getTimestamp());
		else
			return getNoQuestions();
	}
	
	public Iterable<Observation> getSubObservations() {
		// This is O(n*log n)
		List<Observation> observations = new ArrayList<Observation>();
		
		for(int i = 1; i < questions.size(); i++) {
			Observation sub = new Observation(false, form, questions.subList(0, i));
			observations.add(sub);
		}
		return observations;
	}
	public Observation getParent() {
		if(questions.size() > 1)
			return new Observation(false, form, questions.subList(0, questions.size() - 1));
		else
			return this;
	}
	
	public String toString() {
		return questions.toString();
	}
}

































