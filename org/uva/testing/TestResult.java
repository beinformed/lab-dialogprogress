package org.uva.testing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uva.predictions.PredictionUnit;
import org.uva.predictions.Predictor;


public class TestResult {
	private List<Error> errors;
	private Predictor predictor;
	private String lineLabel;
	private PredictionUnit unit;
	
	public TestResult(Predictor predictor, List<Error> errors) {
		this.errors = errors;
		this.predictor = predictor;
		this.lineLabel = predictor.getClass().getSimpleName();
	}
	
	public List<Graph> getErrorToProgressGraph() {
		Map<Integer, Double> timeProgress = new HashMap<Integer, Double>();
		int timeCount = 0;
		Map<Integer, Double> stepsProgress = new HashMap<Integer, Double>();
		int stepsCount = 0;
		
		Graph time = new Graph("Time Error", "Progress (%)", "Error (s)", lineLabel);
		Graph steps = new Graph("Steps Error", "Progress (%)", "Error (steps)", lineLabel);
		for (Error e : errors) {
			int progress = e.getProgressPercentage();
			
			if(e.getUnit() == PredictionUnit.Time) {
				if(!timeProgress.containsKey(progress))
					timeProgress.put(progress, 0.0);
				
				timeProgress.put(progress, timeProgress.get(progress) + e.getError());
				timeCount++;
			}
			else {
				if(!stepsProgress.containsKey(progress))
					stepsProgress.put(progress, 0.0);
				
				stepsProgress.put(progress, stepsProgress.get(progress) + e.getError());
				stepsCount++;
			}
			
			for(int p : timeProgress.keySet()) {
				time.addDataPoint(p, timeProgress.get(p) / timeCount);
			}
			for(int p : stepsProgress.keySet()) {
				time.addDataPoint(p, stepsProgress.get(p) / stepsCount);
			}
		}
		
		List<Graph> result = new ArrayList<Graph>();
		result.add(time);
		result.add(steps);
		return result;
	}
	
	public List<Graph> getErrorToConfidenceGraph() {
		Graph time = new Graph("Time Confidence", "Confidence", "Error (s)", lineLabel);
		Graph steps = new Graph("Steps Confidence", "Confidence", "Error (steps)", lineLabel);
		for (Error e : errors) {
			if(e.getUnit() == PredictionUnit.Time)
				time.addDataPoint(e.getConfidence(), e.getError());
			else
				steps.addDataPoint(e.getConfidence(), e.getError());
		}

		List<Graph> result = new ArrayList<Graph>();
		result.add(time);
		result.add(steps);
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
