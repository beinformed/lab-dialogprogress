package com.beinformed.research.labs.dialogprogress;

import java.util.HashMap;
import java.util.Map;


public class AverageTreePredictor implements Predictor {

	private PredictionUnit unit;
	private Map<Path, PathInfo> paths;
	private int totalFrequency;
	private boolean useAnswers;
	private int maxLength;
	
	public AverageTreePredictor(PredictionUnit unit, boolean useAnswers) {
		this.unit = unit;
		this.paths = new HashMap<Path, PathInfo>();
	}
	
	@Override
	public void train(Iterable<Observation> data) {
		
		for(Observation complete : data) {
			for(Observation part : complete.getSubObservations()) {
				Path path = new Path(part, useAnswers);
				
				updateMap(path, complete, part);
				paths.get(path.getParent()).addChild(path);
				totalFrequency++;
			}
			if(complete.getNoQuestions() > maxLength)
				maxLength = complete.getNoQuestions();
		}	
	}
	
	private void updateMap(Path path, Observation complete, Observation part) {
		if(paths.containsKey(path))
			paths.get(path).add(part, complete);
		else
			paths.put(path, new PathInfo(unit, path, part, complete));
	}

	@Override
	public Prediction predict(Observation data) {
		Path lookup = new Path(data, useAnswers);
		Path parentLookup = lookup.getParent();
		PathInfo info = paths.get(lookup);
		PathInfo parentInfo = paths.get(parentLookup);
		int progress = data.getParent().getLearnValue(unit) - data.getLearnValue(unit);
		
		int parentPrediction = parentInfo.getPrediction() - progress;
		int prediction = info.getPrediction();
		double parentWeight = parentInfo.getFrequency() / (double) totalFrequency;
		double weight = info.getFrequency() / (double) totalFrequency;
		
		int actualPrediction = (int) ((parentPrediction + prediction) / (parentWeight / weight));
		double confidence = getProportion(parentInfo, info) * (data.getNoQuestions() / (double)maxLength);
		
		return new Prediction(confidence, actualPrediction, actualPrediction, unit);
	}
	
	private double getProportion(PathInfo parent, PathInfo child) {
		Iterable<Path> children = parent.getChildren();
		double total = 0;
		
		for(Path c : children) {
			if(!c.equals(child)) {
				total += paths.get(c).getFrequency();
			}
		}
		return child.getFrequency() / total;
	}

	

}
