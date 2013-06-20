package com.beinformed.research.labs.dialogprogress;

import org.neuroph.core.learning.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class NeuralNetworkPredictor implements Predictor {

	private int buckets;
	private PredictionUnit unit;
	private ObservationVectorizer vectorizer;
	private BucketManager bmanager;
	private MultiLayerPerceptron network;

	public NeuralNetworkPredictor(int buckets, PredictionUnit unit) {
		this.unit = unit;
		this.buckets = buckets;
		
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		if(vectorizer == null) {
			vectorizer = new ObservationVectorizer(data);
			bmanager = new BucketManager(buckets, data, unit);
			network = new MultiLayerPerceptron(TransferFunctionType.TANH, vectorizer.getVectorSize(), 10, buckets);
		}
		
		DataSet set = new DataSet(vectorizer.getVectorSize(), buckets);
		for(Observation ob : data) {
			double[] inputVector = vectorizer.getVector(ob);
			double[] bucketVector = bmanager.getBucketVector(ob);
			set.addRow(inputVector, bucketVector);
		}
		
		network.learn(set);
	}

	@Override
	public Prediction predict(Observation data) {
		double[] inputVector = vectorizer.getVector(data);
		
		network.setInput(inputVector);
		network.calculate();
		
		double[] out = network.getOutput();
		double max = Double.NEGATIVE_INFINITY;
		int index = -1;
		
		for(int i = 0; i < out.length; i++) {
			if(out[i] > max)
				index = i;
		}
		
		Bucket b = bmanager.fromVectorIndex(index);
		
		return new Prediction(max, b.lower, b.upper, unit);
	}

}
