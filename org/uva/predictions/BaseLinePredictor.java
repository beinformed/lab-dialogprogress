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
		double remaining = (totalQuestions - questionsSoFar) * avgPerQ;
		
		return new Prediction(defaultConfidence, 
				LongRange.fromConfidence((long) remaining, defaultConfidence), 
				Range.fromConfidence(totalQuestions - questionsSoFar, defaultConfidence));
	}

	@Override
	public void train(Iterable<Observation> data) {
	}
}

