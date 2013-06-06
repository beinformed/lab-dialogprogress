package org.uva.predictions;

public class BaseLinePredictor implements Predictor {
	private double defaultConfidence;
	
	public BaseLinePredictor() {
		this(.5);
	}
	public BaseLinePredictor(double defaultConfidence) {
		this.defaultConfidence = defaultConfidence;
	}
	
	public Prediction predict(Observation data){
		long time = data.getLastAsked().getTimestamp();
		int totalQuestions = data.getForm().getNoQuestions();
		int questionsSoFar = data.getNoQuestions();
		
		double avgPerQ = time / (double)questionsSoFar;
		long remaining = (long) ((totalQuestions - questionsSoFar) * avgPerQ);
		
		return new Prediction(defaultConfidence, 
				new LongRange(remaining, remaining), 
				new Range(totalQuestions - questionsSoFar, totalQuestions - questionsSoFar));
	}

	@Override
	public void train(Iterable<Observation> data) {
	}
}

