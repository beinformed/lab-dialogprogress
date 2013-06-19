package com.beinformed.research.labs.dialogprogress;

/**
 * A predictor for the amount of time and number of steps left on a form.
 *
 */
public interface Predictor {
	/**
	 * Trains the predictor using the provided data. On repeated
	 * calls, the additional data is added to the existing data;
	 * it does not replace it. This method does not return until
	 * the predictor is done training, which can take a long time.
	 * 
	 * @param data
	 * The data to train on.
	 */
	void train(Iterable<Observation> data);
	
	/**
	 * Predicts the total number of to-be-asked questions and the
	 * time it will take the user to answer them with a provided confidence. 
	 * The {@link #train(Iterable) train(Iterable)}
	 * method needs to be called at least once before calling this method.
	 * @param data
	 * The partial observation to predict the outcome of.
	 * @return
	 */
	Prediction predict(Observation data);
}
