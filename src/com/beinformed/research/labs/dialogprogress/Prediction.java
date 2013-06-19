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
	
	public Prediction(double confidence, int lower, int upper, PredictionUnit unit) {
		this.confidence = confidence;
		this.lower = lower;
		this.upper = upper;
		this.unit = unit;
	}
	
	public double getConfidence() {
		return confidence;
	}
	public int getLowerBound() {
		return lower;
	}
	public int getUpperBound() {
		return upper;
	}
	public PredictionUnit getUnit() {
		return unit;
	}
	
	public String toString() {
		return "[" + Integer.toString(lower) + ", " + Integer.toString(upper) + "]";
	}
}
