package com.beinformed.research.labs.dialogprogress;

/**
 * Represents a prediction by an implementation of the {@link com.beinformed.research.labs.dialogprogress.Predictor Predictor} interface.
 * 
 */
public class Prediction {
	private double confidence;
	private int lower;
	private int upper;
	private PredictionUnit unit;
	
	/**
	 * 
	 * @param confidence
	 * The confidence value associated with this prediction. This must be a value between 0 and 1.
	 * @param lower
	 * The lower bound of the prediction (i.e. the real value is expected to be larger than this).
	 * @param upper
	 * The upper bound of the prediction (i.e. the real value is expected to be smaller than this).
	 * @param unit
	 * The unit of this prediction.
	 */
	public Prediction(double confidence, int lower, int upper, PredictionUnit unit) {
		this.confidence = confidence;
		this.lower = lower;
		this.upper = upper;
		this.unit = unit;
	}
	
	/**
	 * Returns the confidence associated with the prediction.
	 * @return
	 */
	public double getConfidence() {
		return confidence;
	}
	/**
	 * Returns the lower bound of this prediction.
	 * @return
	 */
	public int getLowerBound() {
		return lower;
	}
	/**
	 * Returns the upper bound of this prediction.
	 * @return
	 */
	public int getUpperBound() {
		return upper;
	}
	/**
	 * Returns the unit of this prediction.
	 * @return
	 */
	public PredictionUnit getUnit() {
		return unit;
	}
	
	public String toString() {
		return "[" + Integer.toString(lower) + ", " + Integer.toString(upper) + "]";
	}
}
