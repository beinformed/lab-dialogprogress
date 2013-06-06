import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		int count = 0;
		double timeTotal = 0;
		double stepsTotal = 0;
		
		for(DataFold<Observation> f : data) {
			count++;
			Precision e = testFold(p, f);
			timeTotal += e.getTimePrecision();
			stepsTotal += e.getStepsPrecision();
		}	
		
		return new TestResult(p, new Precision(timeTotal / count, stepsTotal / count));
	}

	private Precision testFold(Predictor p, DataFold<Observation> f) {
		double stepsCorrect = 0;
		double timeCorrect = 0;
		int total = 0;
		
		
		p.train(f.getTrainingSet());
		
		for(Observation completeObs : f.getTestSet()) {
			for(Observation o : completeObs.getSubObservations()) {
				Precision e = getError(p, o, completeObs);
				stepsCorrect += e.getStepsPrecision();
				timeCorrect += e.getTimePrecision();
				total++;
			}
		}
		
		return new Precision(timeCorrect / total, stepsCorrect / total);
	}

	private Precision getError(Predictor p, Observation o, Observation actual) {
		Prediction predicted = p.predict(o);
		
		Question lastQuestion = actual.getLastAsked();
		long timeError = predicted.getEstimatedTimeLeft().getDifference(lastQuestion.getTimestamp());				
		int stepsError = predicted.getEstimatedStepsLeft().getDifference(actual.getNoQuestions());
		
		return new Precision((lastQuestion.getTimestamp() - timeError) / (double)lastQuestion.getTimestamp(), 
				(actual.getNoQuestions() - stepsError) / (double)actual.getNoQuestions());
	}
}
























