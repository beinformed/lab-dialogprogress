package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ObservationVectorizer {
	private class QuestionDef {
		private List<String> values = new ArrayList<String>();
		private double max = Double.NEGATIVE_INFINITY;
		private boolean isNumeric = true;
		private boolean isBoolean = true;
		
		public final int index;

		public QuestionDef(int index) {
			this.index = index;
		}
		public void addValue(String value) {
			if(!values.contains(value))
				values.add(value);
			
			if(isNumeric) {
				try {
					double nValue = Double.parseDouble(value);
					if(nValue > max)
						max = nValue;
				} catch(NumberFormatException ex) {
					// not numeric
					isNumeric = false;
				}
			}
			if(isBoolean) {
				try {
					Boolean.parseBoolean(value);
				} catch(NumberFormatException ex) {
					isBoolean = false;
				}
			}
		}
		public double getNumericValue(String value) {
			if(isNumeric)
				return Double.parseDouble(value) / max + 1;
			else if(isBoolean)
				return Boolean.parseBoolean(value) ? 2 : 1;
			
			return values.indexOf(value) / (double)values.size() + 1;
		}
	}
	
	private Map<String, QuestionDef> definitions;
	public ObservationVectorizer(Iterable<Observation> data) {
		Map<String, QuestionDef> definitions = new HashMap<String, QuestionDef>();
		
		int index = 0;
		for(Observation ob : data) {
			for(Question q : ob.getQuestions()) {
				if(!definitions.containsKey(q.getId()))
					definitions.put(q.getId(), new QuestionDef(index++));
				
				definitions.get(q.getId()).addValue(q.getAnswer());
			}
		}
		this.definitions = definitions;
	}
	
	public int getVectorSize() {
		return definitions.size();
	}
	public double[] getVector(Observation data) {
		double[] result = new double[definitions.size()];
		
		for(Question q : data.getQuestions()) {
			QuestionDef def = definitions.get(q.getId());
			result[def.index] = def.getNumericValue(q.getAnswer());
		}
		
		return result;
	}
}











































