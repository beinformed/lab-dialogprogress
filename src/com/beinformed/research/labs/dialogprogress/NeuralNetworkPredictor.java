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
	private int[] layers;

	public NeuralNetworkPredictor(PredictionUnit unit, int buckets, int... layers) {
		this.unit = unit;
		this.buckets = buckets;
		this.layers = layers;
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		if(vectorizer == null) {
			vectorizer = new ObservationVectorizer(data);
			bmanager = new BucketManager(buckets, data, unit);
			
			int[] layers = new int[this.layers.length + 2];
			layers[0] = vectorizer.getVectorSize();
			for(int i = 0; i < this.layers.length; i++)
				layers[i+1] = this.layers[i];
			layers[this.layers.length + 1] = buckets;
			network = new MultiLayerPerceptron(TransferFunctionType.TANH, layers);
		}
		
		DataSet set = new DataSet(vectorizer.getVectorSize(), buckets);
		for(Observation ob : data) {
			for(Observation sub : ob.getSubObservations()) {
				double[] inputVector = vectorizer.getVector(sub);
				double[] bucketVector = bmanager.getBucketVector(ob);
				set.addRow(inputVector, bucketVector);
			}
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
	
	public String toString() {
		String layerString = "[" + layers[0];
		for(int i = 1; i < layers.length; i++)
			layerString += "|" + layers[i];
		
		return "Neural Network " + layerString + "]";
	}

}
