package org.uva.predictions;
import java.util.Iterator;

public interface Predictor {
	void train(Iterator<Observation> data);
	Prediction predict(Observation data);
}
