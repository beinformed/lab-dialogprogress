package org.uva.predictions;

/**
 * Represents a prediction by an implementation of the {@link org.uva.predictions.Predictor Predictor} interface.
 * 
 */
public class Prediction {
	private double confidence;
	private long duration;
	private int steps;
	
	public Prediction(double confidence, long duration, int steps) {
		this.confidence = confidence;
		this.duration = duration;
		this.steps = steps;
	}
	
	public double getConfidence() {
		return confidence;
	}
	public long getEstimatedTimeLeft() {
		return duration;
	}
	public int getEstimatedStepsLeft() {
		return steps;
	}
}
