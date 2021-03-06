package com.beinformed.research.labs.dialogprogress.testing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beinformed.research.labs.dialogprogress.PredictionUnit;
import com.beinformed.research.labs.dialogprogress.Predictor;


public class TestResult {
	final int timeThreshold = 100;
	final int stepsThreshold = 1;
	
	private List<Error> errors;
	private Predictor predictor;
	private String lineLabel;
	
	public TestResult(Predictor predictor, List<Error> errors) {
		this.errors = errors;
		this.predictor = predictor;
		this.lineLabel = predictor.toString();
	}
	
	public List<Graph> getErrorToProgressGraph() {
		Map<Integer, Double> timeProgress = new HashMap<Integer, Double>();
		Map<Integer, Integer> timeCount = new HashMap<Integer, Integer>();
		Map<Integer, Double> stepsProgress = new HashMap<Integer, Double>();
		Map<Integer, Integer> stepsCount = new HashMap<Integer, Integer>();
		
		Graph time = new Graph("Time Error", "Progress (steps)", "Error (s)", lineLabel);
		Graph steps = new Graph("Steps Error", "Progress (steps)", "Error (steps)", lineLabel);
		for (Error e : errors) {
			int progress = e.getUnit() == PredictionUnit.Milliseconds ? e.getProgressPercentage() / 10 * 10 : e.getAbsolutePathLength();
			progress = e.getAbsolutePathLength();
			
			if(e.getUnit() == PredictionUnit.Milliseconds) {
				if(!timeProgress.containsKey(progress)) {
					timeProgress.put(progress, 0.0);
					timeCount.put(progress, 0);
				}
				
				timeProgress.put(progress, timeProgress.get(progress) + e.getError());
				timeCount.put(progress, timeCount.get(progress) + 1);
			}
			else {
				if(!stepsProgress.containsKey(progress)) {
					stepsProgress.put(progress, 0.0);
					stepsCount.put(progress, 0);
				}
				
				stepsProgress.put(progress, stepsProgress.get(progress) + e.getError());
				stepsCount.put(progress, stepsCount.get(progress) + 1);
			}
		}
		
		for(int p : timeProgress.keySet()) {
			time.addDataPoint(p, timeProgress.get(p) / timeCount.get(p));
		}
		for(int p : stepsProgress.keySet()) {
			steps.addDataPoint(p, stepsProgress.get(p) / stepsCount.get(p));
		}
		
		List<Graph> result = new ArrayList<Graph>();
		result.add(time);
		result.add(steps);
		return result;
	}
	
	public List<Graph> getErrorToConfidenceGraph() {
		Map<Double, Integer> timeMap = new HashMap<Double, Integer>();
		Map<Double, Integer> stepsMap = new HashMap<Double, Integer>();
		Map<Double, Integer> timeCount = new HashMap<Double, Integer>();
		Map<Double, Integer> stepsCount = new HashMap<Double, Integer>();
		
		Graph time = new Graph("Time Confidence", "Confidence", "Probability correct", lineLabel);
		Graph steps = new Graph("Steps Confidence", "Confidence", "Probability correct", lineLabel);
		for (Error e : errors) {
			double conf = (int)(e.getConfidence() * 10) / (double)10;
			
			if(e.getUnit() == PredictionUnit.Milliseconds) {
				if(!timeMap.containsKey(conf)) {
					timeMap.put(conf, 0);
					timeCount.put(conf, 0);
				}
				
				if(e.getError() <= timeThreshold) {
					timeMap.put(conf, timeMap.get(conf) + 1);
				}
				timeCount.put(conf, timeCount.get(conf) + 1);
			}
			else {
				if(!stepsMap.containsKey(conf)) {
					stepsMap.put(conf, 0);
					stepsCount.put(conf, 0);
				}
				
				if(e.getError() < stepsThreshold) {
					stepsMap.put(conf, stepsMap.get(conf) + 1);
				}
				stepsCount.put(conf,  stepsCount.get(conf) + 1);
			}
		}

		for(double c : timeMap.keySet()) {
			time.addDataPoint(c, timeMap.get(c) / (double)timeCount.get(c));
		}
		for(double c : stepsMap.keySet()) {
			steps.addDataPoint(c, stepsMap.get(c) / (double)stepsCount.get(c));
		}
		
		List<Graph> result = new ArrayList<Graph>();
		result.add(steps);
		result.add(time);
		return result;
	}
	
	public List<Graph> getAllGraphs() {
		List<Graph> result = new ArrayList<Graph>();
		result.addAll(getErrorToProgressGraph());
		result.addAll(getErrorToConfidenceGraph());
		
		return result;
	}
	
	public Predictor getPredictor() {
		return predictor;
	}
}
