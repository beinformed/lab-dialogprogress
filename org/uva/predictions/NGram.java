package org.uva.predictions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class NGram{
	private String[] words;
	private String string;
	
	public NGram(Iterable<Question> questions, EnumSet<ValueType> type) {
		List<String> ngram = new ArrayList<String>();
		long first = -1;
		
		for(Question q : questions) {
			if(first == -1)
				first = q.getTimestamp();
			ngram.add(getString(q, type, first));
		}
		this.words = ngram.toArray(new String[0]);
	}
	
	private String getString(Question q, EnumSet<ValueType> type, long first) {
		String result = "";
		
		if(type.contains(ValueType.TimeStamp))
			result += q.getTimestamp() - first;
		if(type.contains(ValueType.Question))
			result += q.getId();
		if(type.contains(ValueType.Answer))
			result += q.getAnswer();
		if(type.contains(ValueType.Status))
			result += ""; // TODO: add status
		
		return result;
	}

	@Override
	public boolean equals( Object  o ){
		return toString().equals(o.toString());
	}

	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	
	@Override		
	public String toString(){
		if(string != null)
			return string;
		
		String result = words[0];
		for(int i = 1; i < words.length; i++)
			result += "," + words[i];
		string = result;
		return result;
	}
}
