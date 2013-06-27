package com.beinformed.research.labs.dialogprogress;

/**
 * This predictor uses the maximum number of steps to make its predictions.
 * For predicting time, it returns (average time per question) * (maximum number of questions) - (time so far).
 * For predicting steps, it returns (maximum number of questions) - (questions so far).
 * 
 * This algorithm is very fast to train, but gives inaccurate results.
 *
 */
public class BaselinePredictor implements Predictor {
	private PredictionUnit unit;
	int maxNrQuestions;	

	/**
	 * 
	 * @param unit The unit to predict.
	 */
	public BaselinePredictor(PredictionUnit unit) {
		this.unit = unit;
	}
	
	public Prediction predict(Observation data){		
		int curNrQ = data.getNoQuestions();
		long timeTaken = data.getLearnValue(PredictionUnit.Milliseconds);
		int questionAvrg = ( int ) ( timeTaken / curNrQ ) ;
		int maxRemainingQ = maxNrQuestions - curNrQ;
		int predictionT = questionAvrg * maxRemainingQ;

		double confidence = curNrQ / (double)maxNrQuestions;
		if ( unit == PredictionUnit.Milliseconds )
			return new Prediction(confidence, predictionT, predictionT, unit);
		else
			return new Prediction(confidence, maxRemainingQ, maxRemainingQ, unit);
	}

	@Override	
	public void train(Iterable<Observation> data) {
		for(Observation obs : data) {			
			if ( maxNrQuestions < obs.getNoQuestions())
				maxNrQuestions = obs.getNoQuestions();
		}
	}
	
	public String toString() {
		return "Baseline";
	}
}


