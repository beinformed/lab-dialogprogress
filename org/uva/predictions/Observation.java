package org.uva.predictions;

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
	private Iterable<Question> questions;
	private Question last;
	private Question first;
	private int noQuestions;
	
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
		this.questions = questions;
		
		Question first = null;
		for(Question q : questions) {
			if(first == null)
				first = q;
			last = q;
			noQuestions++;
		}
		this.first = first;
		
		if(finished && getLearnValue(PredictionUnit.Time) < 0) {
			System.err.println(questions);
		}
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
		return noQuestions;
	}
	/**
	 * Returns the last question the user answered.
	 * @return
	 */
	public Question getLastAsked() {
		return last;
	}
	public Question getFirstAsked() {
		return first;
	}
	
	public int getLearnValue(PredictionUnit unit) {
		if(unit == PredictionUnit.Time)
			return (int) (last.getTimestamp() - first.getTimestamp());
		else
			return noQuestions;
	}
	
	public Iterable<Observation> getSubObservations() {
		// This is O(n*log n)
		List<Observation> observations = new ArrayList<Observation>();
		List<Question> part = new ArrayList<Question>();
		for(Question q : questions) {
			part.add(q);
			observations.add(new Observation(false, form, copy(part)));
		}
		
		return observations;
	}
	
	private Iterable<Question> copy(Collection<Question> original) {
		Question[] all = new Question[original.size()];
		all = original.toArray(all); // guaranteed to return a new array, not the internal one
		return Arrays.asList(all);
	}
}

































