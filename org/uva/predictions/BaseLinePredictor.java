package org.uva.predictions;

class BaseLinePredictor implements Predictor {
	
	public Prediction predict(Observation data){
		long time = data.getLastAsked().getTimestamp();
		int totalQuestions = data.getForm().getNoQuestions();
		int questionsSoFar = data.getNoQuestions();
		
		double avgPerQ = time / (double)questionsSoFar;
		double remaining = (totalQuestions - questionsSoFar) * avgPerQ;
		
		return new Prediction(.5, (long)remaining, totalQuestions - questionsSoFar);
	}

	@Override
	public void train(Iterable<Observation> data) {
	}
}

