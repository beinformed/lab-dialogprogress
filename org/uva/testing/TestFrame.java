package org.uva.testing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uva.predictions.*;

public class TestFrame {
	
	private Iterable<Predictor> predictors;
	
	public TestFrame(Iterable<Predictor> predictors) {
		this.predictors = predictors;
	}
	
	public Iterable<TestResult> testAll(List<Observation> data) {
		Collection<DataFold<Observation>> split = DataFold.allFromList(data, 5);
		List<TestResult> results = new ArrayList<TestResult>();
		
		for(Predictor p : predictors)
			results.add(testPredictor(p, split));
		
		return results;
	}
	
	private TestResult testPredictor(Predictor p, Collection<DataFold<Observation>> data) {
		Map<Integer, Error> pathSizeToError = new HashMap<Integer, Error>();
		
		for(DataFold<Observation> f : data) {
			Map<Integer, Error> errors = testFold(p, f);
			
			for(Integer key : errors.keySet()) {
				Error error = errors.get(key);
				
				if(!pathSizeToError.containsKey(key))
					pathSizeToError.put(key, new Error(error.getUnit()));
				
				pathSizeToError.get(key).add(error);
			}
		}	
		
		return new TestResult(p, pathSizeToError);
	}

	private Map<Integer, Error> testFold(Predictor p, DataFold<Observation> f) {
		Map<Integer, Error> pathSizeToError = new HashMap<Integer, Error>();
		
		p.train(f.getTrainingSet());
		
		for(Observation actual : f.getTestSet()) {
			for(Observation o : actual.getSubObservations()) {
				Prediction predicted = p.predict(o);
				PredictionUnit unit = predicted.getUnit();
				int correctValue = actual.getLearnValue(unit) - o.getLearnValue(unit);
				
				if(!pathSizeToError.containsKey(o.getNoQuestions()))
					pathSizeToError.put((Integer)o.getNoQuestions(), new Error(unit));
				
				int error = getError(predicted, correctValue);
				
				pathSizeToError.get((Integer)o.getNoQuestions()).add(error);
			}
		}
		
		return pathSizeToError;
	}

	private int getError(Prediction predicted, int correctValue) {
		int lower = predicted.getLowerBound();
		int upper = predicted.getUpperBound();
		int result = 0;
		
		if(correctValue < lower)
			result = (lower - correctValue)^2;
		else if(correctValue > upper)
			result = (correctValue - upper)^2;
		
		return (int) Math.sqrt(result);
	}
}
























