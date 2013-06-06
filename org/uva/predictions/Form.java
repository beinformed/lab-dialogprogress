package org.uva.predictions;

/**
 * Represents a form with questions.
 * 
 *
 */
public class Form {
	private int id;
	private int noQuestions;
	
	/**
	 * 
	 * @param id
	 * Some id that is unique for each form. Used to train predictors on a
	 * form-by-form basis.
	 * @param noQuestions
	 * The total number of questions on this form.
	 */
	public Form(int id, int noQuestions) {
		this.id = id;
		this.noQuestions = noQuestions;
	}
	
	/**
	 * Returns the id for this form.
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * Returns the total number of questions on this form.
	 * @return
	 */
	public int getNoQuestions() {
		return noQuestions;
	}
}
