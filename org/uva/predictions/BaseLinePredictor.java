package org.uva.predictions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseLinePredictor implements Predictor {
	private double defaultConfidence;
	private Map<String, Integer> averages;
	private Map<String, Integer> standardDeviations;
	private PredictionUnit unit;
	
	public BaseLinePredictor(PredictionUnit unit) {
		this(unit, .5);
	}
	public BaseLinePredictor(PredictionUnit unit, double defaultConfidence) {
		this.defaultConfidence = defaultConfidence;
		this.unit = unit;
		this.averages = new HashMap<String, Integer>();
		this.standardDeviations = new HashMap<String, Integer>();
	}
	
	public Prediction predict(Observation data){
		int average = averages.get(data.getForm());
		int deviation = standardDeviations.get(data.getForm());
		int prediction = average - data.getLearnValue(unit);
		
		return new Prediction(defaultConfidence, prediction - deviation, prediction + deviation, unit);
	}

	@Override
	public void train(Iterable<Observation> data) {
		Map<String, List<Integer>> values = getDataValues(data);
		Map<String, Integer> averages = getAverages(values);
		Map<String, Integer> deviations = getDeviations(values, averages);
		
		this.averages = averages;
		this.standardDeviations = deviations;
	}
	private Map<String, List<Integer>> getDataValues(Iterable<Observation> data) {
		Map<String, List<Integer>> values = new HashMap<String, List<Integer>>();
		
		for(Observation obs : data) {
			String form = obs.getForm();
			
			if(!values.containsKey(form))
				values.put(form, new ArrayList<Integer>());
			
			values.get(form).add(obs.getLearnValue(unit));
		}
		
		return values;
	}
	
	private Map<String, Integer> getAverages(Map<String, List<Integer>> values) {
		Map<String, Integer> averages = new HashMap<String, Integer>();
		
		for(String key : values.keySet()) {
			averages.put(key, getAverage(values.get(key)));
		}
		
		return averages;
	}
	private Integer getAverage(List<Integer> list) {
		int total = 0;
		
		for(int i : list) {
			total += i;
		}
		
		return total / list.size();
	}
	
	private Map<String, Integer> getDeviations(
			Map<String, List<Integer>> values, Map<String, Integer> averages) {
		Map<String, Integer> deviation = new HashMap<String, Integer>();
		
		for(String key : averages.keySet()) {
			List<Integer> valueDeviations = getSquareDeviations(averages.get(key), values.get(key));
			deviation.put(key, (int) Math.sqrt(getAverage(valueDeviations)));
		}
		
		return deviation;
	}
	private List<Integer> getSquareDeviations(int average, List<Integer> list) {
		List<Integer> result = new ArrayList<Integer>();
		
		for(int i : list) {
			result.add((average - i)^2);
		}
		return result;
	}
	
}









































