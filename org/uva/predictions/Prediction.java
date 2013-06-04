package org.uva.predictions;

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
