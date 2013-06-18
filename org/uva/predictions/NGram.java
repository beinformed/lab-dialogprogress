package org.uva.predictions;

import java.util.EnumSet;


public class NGram{
	private String ngram;
	
	public NGram(Iterable<Question> questions, EnumSet<ValueType> type) {
		String ngram = "";
		long first = -1;
		
		for(Question q : questions) {
			if(first == -1)
				first = q.getTimestamp();
			ngram += getString(q, type, first) + ",";
		}
		this.ngram = ngram.substring(0, ngram.length() - 1);
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
			result += ""; // TODO: add timestamp
		
		return result;
	}

	@Override
	public boolean equals( Object  o ){
		return ngram.equals(o);
	}

	@Override
	public int hashCode(){
		return ngram.hashCode();
	}
	
	@Override		
	public String toString(){
		return ngram;
	}
}
