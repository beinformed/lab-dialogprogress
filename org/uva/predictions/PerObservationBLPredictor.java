package org.uva.predictions;

import java.util.HashMap;

public class PerObservationBLPredictor implements Predictor {
	private double defaultConfidence;
	private PredictionUnit unit;
	private HashMap<String, Integer> maxNrQuestions;	

	public PerObservationBLPredictor(PredictionUnit unit) {
		this(unit, .5);
	}
	public PerObservationBLPredictor(PredictionUnit unit, double defaultConfidence) {
		maxNrQuestions = new HashMap<String, Integer>();
		this.defaultConfidence = defaultConfidence;
		this.unit = unit;
	}
	
	public Prediction predict(Observation data){		
		int curNrQ = data.getNoQuestions();
		long timeTaken = data.getLearnValue(PredictionUnit.Time);
		int questionAvrg = ( int ) ( timeTaken / curNrQ ) ;
		int maxRemainingQ = maxNrQuestions.get(data.getForm()) - curNrQ;
		int predictionT = questionAvrg * maxRemainingQ;

		double confidence = (curNrQ / (maxRemainingQ+1)) * 100;
		if ( unit == PredictionUnit.Time )
			return new Prediction(confidence, predictionT, predictionT, unit);
	//	else if (unit == PredictionUnit.Steps)
			return new Prediction(confidence, maxRemainingQ, maxRemainingQ, unit);
	}

	@Override
	/**
	 * Method train - counts maximal number of questions in this form
	 */	
	public void train(Iterable<Observation> data) {
		for(Observation obs : data) {
			String form = obs.getForm();
			
			if(!maxNrQuestions.containsKey(form))
				maxNrQuestions.put(form, obs.getNoQuestions());
			
			if ( (int) maxNrQuestions.get(form) < obs.getNoQuestions())
				maxNrQuestions.put(form, obs.getNoQuestions());
		}
	}
	
}


