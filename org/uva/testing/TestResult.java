package org.uva.testing;
import org.uva.predictions.Predictor;


public class TestResult {
	private Precision precision;
	private Predictor predictor;
	
	public TestResult(Predictor predictor, Precision precision) {
		this.precision = precision;
		this.predictor = predictor;
	}
	
	public Precision getPrecision() {
		return precision;
	}
	public Predictor getPredictor() {
		return predictor;
	}
	
	public String toString() {
		return predictor.getClass().getSimpleName() + ": " + precision.toString();
	}
}
