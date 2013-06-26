package com.beinformed.research.labs.dialogprogress;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class NeuralNetworkPredictor implements Predictor {

	private int buckets;
	private PredictionUnit unit;
	private ObservationVectorizer vectorizer;
	private BucketManager bmanager;
	private BasicNetwork network;
	private int[] layers;

	public NeuralNetworkPredictor(PredictionUnit unit, int buckets, int... layers) {
		this.unit = unit;
		this.buckets = buckets;
		this.layers = layers;
		network = new BasicNetwork();
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		if(vectorizer == null) {
			vectorizer = new ObservationVectorizer(data);
			bmanager = new BucketManager(buckets, data, unit);
			
			network.addLayer(new BasicLayer(vectorizer.getVectorSize()));
			for(int i = 0; i < layers.length; i++)
				network.addLayer(new BasicLayer(new ActivationSigmoid(), true, layers[i]));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), false, buckets));
			network.getStructure().finalizeStructure();
			network.reset();
		}
		
		MLDataSet set = new BasicMLDataSet();
		for(Observation ob : data) {
			for(Observation sub : ob.getSubObservations()) {
				MLData input = new BasicMLData(vectorizer.getVector(sub));
				MLData output = new BasicMLData(bmanager.getBucketVector(ob.getLearnValue(unit) - sub.getLearnValue(unit)));
				set.add(input, output);
			}
		}
		
		final ResilientPropagation train = new ResilientPropagation(network, set);
		
		do {
			train.iteration();	
		} while(train.getError() > .09);
		train.finishTraining();
	}

	@Override
	public Prediction predict(Observation data) {		
		MLData obs = new BasicMLData(vectorizer.getVector(data));
		
		double[] out = network.compute(obs).getData();
		double max = Double.NEGATIVE_INFINITY;
		double total = 0;
		int index = -1;
		
		for(int i = 0; i < out.length; i++) {
			total += out[i];
			if(out[i] > max) {
				index = i;
				max = out[i];
			}
		}
		
		Bucket b = bmanager.fromVectorIndex(index);
		
		Encog.getInstance().shutdown();
		
		return new Prediction(max/total, b.lower, b.upper, unit);
	}
	
	public String toString() {
		String layerString = "[" + buckets;
		for(int i = 0; i < layers.length; i++)
			layerString += "|" + layers[i];
		
		return "Neural Network " + layerString + "]";
	}

}
