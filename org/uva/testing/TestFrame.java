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
		Collection<DataFold<Observation>> split = DataFold.allFromList(data, 3);
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
				if(!pathSizeToError.containsKey(key))
					pathSizeToError.put(key, new Error());
				
				pathSizeToError.get(key).add(errors.get(key));
			}
		}	
		
		return new TestResult(p, pathSizeToError);
	}

	private Map<Integer, Error> testFold(Predictor p, DataFold<Observation> f) {
		Map<Integer, Error> pathSizeToError = new HashMap<Integer, Error>();
		
		p.train(f.getTrainingSet());
		
		for(Observation actual : f.getTestSet()) {
			for(Observation o : actual.getSubObservations()) {
				if(!pathSizeToError.containsKey(o.getNoQuestions()))
					pathSizeToError.put((Integer)o.getNoQuestions(), new Error());
				
				Prediction predicted = p.predict(o);
				
				Question lastQuestion = actual.getLastAsked();
				long timeError = predicted.getEstimatedTimeLeft().getDifference(lastQuestion.getTimestamp());				
				int stepsError = predicted.getEstimatedStepsLeft().getDifference(actual.getNoQuestions());
				pathSizeToError.get((Integer)o.getNoQuestions()).add(stepsError, timeError);
			}
		}
		
		return pathSizeToError;
	}
}
























