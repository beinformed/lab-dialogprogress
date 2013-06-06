package org.uva.predictions;

public interface Predictor {
	void train(Iterable<Observation> data);
	Prediction predict(Observation data);
}
