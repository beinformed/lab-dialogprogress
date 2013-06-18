package com.beinformed.research.labs.dialogprogress.testing;

import com.beinformed.research.labs.dialogprogress.PredictionUnit;

public class Error {
	private double error;
	private PredictionUnit unit;
	private int totalPath;
	private int pathSoFar;
	private double confidence;
	
	public Error(PredictionUnit unit, int pathSoFar, int totalPath, double error, double confidence) {
		this.totalPath = totalPath;
		this.pathSoFar = pathSoFar;
		this.error = error;
		this.unit = unit;
		this.confidence = confidence;
	}
	
	public double getError() {
		return error;
	}
	public PredictionUnit getUnit() {
		return unit;
	}
	
	public int getProgressPercentage() {
		return (int) (pathSoFar / (double)totalPath * 100);
	}
	
	public double getConfidence() {
		return confidence;
	}
	
	public String toString() {
		return Double.toString(getError()) + " (" + unit.toString() + ").";
	}
}
