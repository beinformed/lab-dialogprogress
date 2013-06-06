package org.uva.predictions;

/**
 * Represents a prediction by an implementation of the {@link org.uva.predictions.Predictor Predictor} interface.
 * 
 */
public class Prediction {
	private double confidence;
	private LongRange duration;
	private Range steps;
	
	public Prediction(double confidence, LongRange duration, Range steps) {
		this.confidence = confidence;
		this.duration = duration;
		this.steps = steps;
	}
	
	public double getConfidence() {
		return confidence;
	}
	public LongRange getEstimatedTimeLeft() {
		return duration;
	}
	public Range getEstimatedStepsLeft() {
		return steps;
	}
}
