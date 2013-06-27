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

/**
 * This predictor uses a neural network using backpropagation to make its predictions.
 * It requires the Encog library to function.
 * 
 * Depending on the parameters, this algorithm can be fast to train, or more
 * accurate. If the model is trained on unpredictable data, the algorithm will
 * not converge to a solution and {@link #train(Iterable)} will block forever.
 * 
 * The algorithm does not support adding new questions in between training sessions.
 * It is recommended to always make the first call to {@link #train(Iterable)} contain
 * a representable dataset, as the first call determines the questions that can be
 * asked.
 * 
 * @see http://www.heatonresearch.com/encog
 *
 */
public class NeuralNetworkPredictor implements Predictor {

	private int buckets;
	private PredictionUnit unit;
	private ObservationVectorizer vectorizer;
	private BucketManager bmanager;
	private BasicNetwork network;
	private int[] layers;

	/**
	 * 
	 * @param unit
	 * The unit of the predictions.
	 * @param buckets
	 * The number of buckets to distribute the data over. More buckets will lead to
	 * more fine-grained results, but might lead to smaller precision.
	 * @param layers
	 * The number of nodes for each layer. The structure of the final network will
	 * be [number of questions] - [provided layers] - [number of buckets].
	 */
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
		} while(train.getError() > .075);
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
