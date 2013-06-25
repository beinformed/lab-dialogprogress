package com.beinformed.research.labs.dialogprogress;

public class PerObservationBLPredictor implements Predictor {
	private PredictionUnit unit;
	int maxNrQuestions;	

	public PerObservationBLPredictor(PredictionUnit unit) {
		this.unit = unit;
	}
	
	public Prediction predict(Observation data){		
		int curNrQ = data.getNoQuestions();
		long timeTaken = data.getLearnValue(PredictionUnit.Time);
		int questionAvrg = ( int ) ( timeTaken / curNrQ ) ;
		int maxRemainingQ = maxNrQuestions - curNrQ;
		int predictionT = questionAvrg * maxRemainingQ;

		double confidence = curNrQ / (double)maxNrQuestions;
		if ( unit == PredictionUnit.Time )
			return new Prediction(confidence, predictionT, predictionT, unit);
		else
			return new Prediction(confidence, maxRemainingQ, maxRemainingQ, unit);
	}

	@Override
	/**
	 * Method train - counts maximal number of questions in this form
	 */	
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


