package com.beinformed.research.labs.dialogprogress;

import java.util.HashMap;
import java.util.Map;


public class AverageTreePredictor implements Predictor {

	private PredictionUnit unit;
	private Map<Path, PathInfo> paths;
	private Map<Path, PathInfo> pathsWithAnswers;
	private int totalFrequency;
	private int maxLength;
	private double aWeight, noaWeight, pWeight;
	
	public AverageTreePredictor(PredictionUnit unit, double aWeight, double noaWeight, double pWeight) {
		this.unit = unit;
		this.paths = new HashMap<Path, PathInfo>();
		this.pathsWithAnswers = new HashMap<Path, PathInfo>();
		this.aWeight = aWeight;
		this.noaWeight = noaWeight;
		this.pWeight = pWeight;
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		
		for(Observation complete : data) {
			for(Observation part : complete.getSubObservations()) {
				Path path = new Path(part, false);
				Path pathWithAnswers = new Path(part, true);
				
				updateMap(paths, path, complete, part);
				updateMap(pathsWithAnswers, pathWithAnswers, complete, part);
				paths.get(path.getParent()).addChild(path);
				pathsWithAnswers.get(pathWithAnswers.getParent()).addChild(pathWithAnswers);
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
		PathInfo infoNoAnswers = paths.get(new Path(data, false));
		PathInfo infoWithAnswers = paths.get(new Path(data, true));
		PathInfo infoParent = paths.get(infoNoAnswers.getParentPath());
		
		double weightNoAnswers = getWeight(infoNoAnswers) * noaWeight;
		double weightWithAnswers = getWeight(infoWithAnswers) * aWeight;
		double weightParent = getWeight(infoParent) * pWeight;
		
		double prediction = (
				(getPrediction(infoNoAnswers) * weightNoAnswers) +
				(getPrediction(infoWithAnswers) * weightWithAnswers) +
				((getPrediction(infoParent) - (data.getParent().getLearnValue(unit) - data.getLearnValue(unit))) * weightParent))
				/
				(weightNoAnswers + weightWithAnswers + weightParent);
		
		double confidence = getConfidence(data);
		
		return new Prediction(confidence, (int)prediction, (int)prediction, unit);
	}
	
	private double getWeight(PathInfo info) {
		if(info == null)
			return 0;
		else
			return info.getFrequency() / (double)totalFrequency;
	}
	private double getPrediction(PathInfo info) {
		if(info == null)
			return 0;
		else
			return info.getPrediction();
	}
	
	private double getConfidence(Observation data) {
		return (1 - (1 / ((double)totalFrequency / 1000))) * (data.getNoQuestions() / (double)maxLength);
	}

	public String toString() {
		return "Average Tree [" + aWeight + "|" + noaWeight + "|" + pWeight + "]";
	}

}
