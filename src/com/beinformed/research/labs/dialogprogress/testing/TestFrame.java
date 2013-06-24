package com.beinformed.research.labs.dialogprogress.testing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.beinformed.research.labs.dialogprogress.*;

public class TestFrame {
	
	private Iterable<PredictorFactory> predictors;
	
	public TestFrame(List<PredictorFactory> predictors) {
		this.predictors = predictors;
	}
	
	public Iterable<TestResult> testAllFolds(List<Observation> data, int folds) {
		Collection<DataFold<Observation>> split = DataFold.allFromList(data, folds);
		List<TestResult> results = new ArrayList<TestResult>();
		
		for(PredictorFactory p : predictors)
			results.add(testPredictor(p, split));
		
		return results;
	}
	public Iterable<TestResult> testAll(List<Observation> train, List<Observation> test) {
		DataFold<Observation> fold = new DataFold<Observation>(test, train);
		List<DataFold<Observation>> wrap = new ArrayList<DataFold<Observation>>();
		wrap.add(fold);
		
		List<TestResult> results = new ArrayList<TestResult>();
		for(PredictorFactory p : predictors) 
			results.add(testPredictor(p, wrap));
		
		return results;
	}
	
	private TestResult testPredictor(PredictorFactory p, Collection<DataFold<Observation>> data) {
		List<Error> errors = new ArrayList<Error>();
		
		for(DataFold<Observation> f : data) {
			List<Error> foldErrors = testFold(p.getPredictor(), f);
			
			errors.addAll(foldErrors);
		}	
		
		return new TestResult(p.getPredictor(), errors);
	}

	private List<Error> testFold(Predictor p, DataFold<Observation> f) {
		System.out.println("Now testing " + p.toString());
		List<Error> errors = new ArrayList<Error>();
		
		p.train(f.getTrainingSet());
		
		for(Observation actual : f.getTestSet()) {
			for(Observation o : actual.getSubObservations()) {
				Prediction predicted = p.predict(o);
				PredictionUnit unit = predicted.getUnit();
				int correctValue = actual.getLearnValue(unit) - o.getLearnValue(unit);
				
				Error error = new Error(unit, o.getNoQuestions(), actual.getNoQuestions(),
						getError(predicted, correctValue), predicted.getConfidence());
				errors.add(error);
			}
		}

		return errors;
	}

	private double getError(Prediction predicted, int correctValue) {
		int lower = predicted.getLowerBound();
		int upper = predicted.getUpperBound();
		int result = 0;
		
		if(correctValue < lower)
			result = (lower - correctValue)^2;
		else if(correctValue > upper)
			result = (correctValue - upper)^2;
		
		return Math.sqrt(result);
	}
}
























