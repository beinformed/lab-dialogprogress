package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayList;
import java.util.List;
/**
 * A predictor for the amount of time and number of steps left on a form.
 *
 */
public class HighestConfidenceAggregator implements Predictor {

	private PredictionUnit unit;
	private List<Predictor> predictors;

	public HighestConfidenceAggregator( PredictionUnit unit ){
		this.unit = unit;
		predictors = new ArrayList<Predictor>();
		predictors.add(new PerObservationBLPredictor(unit));	
		
	}

	public void train(Iterable<Observation> data){
		for ( Predictor predictor : predictors )
			predictor.train(data);	

	}
	
	public Prediction predict(Observation data){
		
		Prediction prediction = new Prediction( 0,0,0,unit); 
		for ( Predictor predictor : predictors ){
			Prediction predict = predictor.predict(data);
			if ( predict.getConfidence() > prediction.getConfidence())
				prediction = predict;
		}	
		return prediction;
	}
} 
