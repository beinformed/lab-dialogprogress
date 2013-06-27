package com.beinformed.research.labs.dialogprogress;

import java.util.HashMap;
import java.util.Map;

/**
 * This predictor keeps track of all paths, their frequencies and the average of the
 * time or steps remaining at that point. It predicts a weighted average of the remaining
 * time predicted by the current node with answers in the path, the current node without answers
 * in the path and the parent node.
 * 
 * The algorithm is reasonably fast to train. It requires a lot of data to give reasonable
 * predictions for all paths.
 */
public class AverageTreePredictor implements Predictor {

	private PredictionUnit unit;
	private Map<Path, PathInfo> paths;
	private int totalFrequency;
	private int maxLength;
	private double aWeight, noaWeight, pWeight;
	
	/**
	 * 
	 * @param unit
	 * The unit of the predictions
	 * @param aWeight
	 * The weight of the paths with answers for predictions. Setting this high is
	 * advisable if there are a lot of questions where the answer determines forks
	 * in the tree of possible paths.
	 * @param noaWeight
	 * The weight of the paths with no answers for predictions. Setting this high
	 * is advisable if there are not many forks, or if there are many open questions
	 * (such as 'what is your name').
	 * @param pWeight
	 * The weight of the parent node for predictions. Setting this high is advisable
	 * if there are many uncommon paths.
	 */
	public AverageTreePredictor(PredictionUnit unit, double aWeight, double noaWeight, double pWeight) {
		this.unit = unit;
		this.paths = new HashMap<Path, PathInfo>();
		this.aWeight = aWeight;
		this.noaWeight = noaWeight;
		this.pWeight = pWeight;
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		
		for(Observation complete : data) {
			for(Observation part : complete.getSubObservations()) {
				// part is a partial observation
				
				Path path = new Path(part, false);
				Path pathWithAnswers = new Path(part, true);
				
				// safe to save both types of paths (with/no answers) in the
				// same hashmap because withAnswers.equals(noAnswers) will never be true.
				updateMap(paths, path, complete, part);
				updateMap(paths, pathWithAnswers, complete, part);
				paths.get(path.getParent()).addChild(path);
				paths.get(pathWithAnswers.getParent()).addChild(pathWithAnswers);
				totalFrequency++;
			}
			if(complete.getNoQuestions() > maxLength)
				maxLength = complete.getNoQuestions();
		}	
	}
	
	private void updateMap(Map<Path, PathInfo> map, Path path, Observation complete, Observation part) {
		if(map.containsKey(path))
			map.get(path).add(part, complete);
		else
			map.put(path, new PathInfo(unit, path, part, complete));
	}

	@Override
	public Prediction predict(Observation data) {
		Path noAnswers = new Path(data, false);
		Path withAnswers = new Path(data, true);
		Path parent = noAnswers.getParent();
		
		PathInfo infoNoAnswers = paths.get(noAnswers);
		PathInfo infoWithAnswers = paths.get(withAnswers);
		PathInfo infoParent = paths.get(parent);
		
		double weightNoAnswers = getWeight(infoNoAnswers) * noaWeight;
		double weightWithAnswers = getWeight(infoWithAnswers) * aWeight;
		double weightParent = getWeight(infoParent) * pWeight;
		
		double prediction = (
				(getPrediction(infoNoAnswers) * weightNoAnswers) +
				(getPrediction(infoWithAnswers) * weightWithAnswers) +
				((getPrediction(infoParent) - (data.getParent().getLearnValue(unit) - data.getLearnValue(unit))) * weightParent))
				/
				(weightNoAnswers + weightWithAnswers + weightParent);
		
		double confidence = getConfidence(data, infoNoAnswers != null ? infoNoAnswers.getFrequency() : 0);
		return new Prediction(confidence, (int)prediction, (int)prediction, unit);
	}
	
	private double getWeight(PathInfo info) {
		// java's HashMap returns null if it can't find a key.
		// It is reasonable to expect paths cannot always be found (especially
		// for paths with answers).
		if(info == null)
			return 0;
		else
			return 1;
	}
	private double getPrediction(PathInfo info) {
		if(info == null)
			return 0;
		else
			return info.getPrediction();
	}
	
	private double getConfidence(Observation data, int nodefreq) {
		// confidence depends on the progress, total amount of data in
		// the model and the amount of data in the current node.
		double progress = (data.getNoQuestions() / (double)maxLength);
		double order = getOrderOfMagnitude(totalFrequency);
		
		double dataSize = 1/order;
		double nodeSize = 1.0/getOrderOfMagnitude(nodefreq);
		
		return .3 * (1 - dataSize) + .2 * progress + .5 * nodeSize;
	}
	
	private int getOrderOfMagnitude(final int value) {
		int order = 1;
		int tmp = value;
		while(tmp != 0) {
			order *= 10;
			tmp = tmp / 10;
		}
		return order > 1 ? order/10 : order;
	}

	public String toString() {
		return "Average Tree [" + aWeight + "|" + noaWeight + "|" + pWeight + "]";
	}

}
