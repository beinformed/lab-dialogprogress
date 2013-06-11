package org.uva.testing;
import java.util.Map;

import org.uva.predictions.Predictor;


public class TestResult {
	private Map<Integer, Error> averageError;
	private Predictor predictor;
	
	public TestResult(Predictor predictor, Map<Integer, Error> pathSizeToError) {
		this.averageError = pathSizeToError;
		this.predictor = predictor;
	}
	
	public Map<Integer, Error> getAverageError() {
		return averageError;
	}
	public Predictor getPredictor() {
		return predictor;
	}
	
	public String toString() {
		String results = "";
		
		for(Integer key : averageError.keySet()) {
			results += "\nPath length " + key.toString() + " gives error " + averageError.get(key).toString();
		}
		
		return predictor.getClass().getSimpleName() + ": " + results;
	}
}
