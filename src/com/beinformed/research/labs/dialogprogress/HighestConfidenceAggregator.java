package com.beinformed.research.labs.dialogprogress;

/**
 * This predictor uses the confidence value used by other predictors to return
 * the best prediction. The prediction with the highest confidence is always
 * returned. No scaling is applied to the confidence values to make them more
 * comparable.
 * 
 * Training time can take long, because the {@link #train(Iterable)} method is 
 * called for each of the internal predictors.
 *
 */
public class HighestConfidenceAggregator implements Predictor {

	private PredictionUnit unit;
	private Iterable<Predictor> predictors;

	/**
	 * 
	 * @param unit
	 * The unit of the predictions.
	 * @param predictors
	 * The predictors to use for predictions.
	 */
	public HighestConfidenceAggregator( PredictionUnit unit, Iterable<Predictor> predictors ){
		this.unit = unit;
		this.predictors = predictors;		
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
