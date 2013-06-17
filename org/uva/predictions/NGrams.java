package org.uva.predictions;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class NGrams{
	
	
	Map<NGram,Integer> nGramCounts;
	Map<NGram,Integer> nM1GramCounts;
	Map<NGram, Double> nGramProbs;
		
	/**
	 *
	 * assumes only 1 form is present
	 */
	public NGrams( Iterable<Observation> data, ValueType type, int n ){
		buildModel(data, n);

//		switch (type) {
//			case Question : nGramCounts = setStringGrams(data, n);
//			case TimeStamp : setLongGrams(data, n);
//		} 
	}

	private void buildModel( Iterable<Observation> data, int n ){
		nGramCounts = setStringGrams(data, n );
		nM1GramCounts = setStringGrams(data, n-1 );
		nGramProbs = calcProbabilities(n);
	}

	private Map<NGram, Integer> setStringGrams( Iterable<Observation> data, int n ){
	
		Map<NGram, Integer> counts = new HashMap<NGram, Integer>();
		for (Observation obs : data){
			int i = 1;	
			ArrayDeque<String> valueQ = new ArrayDeque<String>();
			valueQ.offerFirst("/s");
			for ( Question q : obs.getQuestions()  ){
				valueQ.offerLast( q.getId() );
				if (++i >= n){
					counts = count(valueQ, counts, n);
					String s = valueQ.pollFirst();
				}
			}
			valueQ.offerLast("/e");
			counts = count(valueQ, counts, n);
		}
		return counts;
	}

	private Map<NGram, Double> calcProbabilities(int n){
		Map<NGram, Double> probs = new HashMap<NGram, Double>();
		for( NGram ngram : nGramCounts.keySet() ){
			double nC = (double) nGramCounts.get( ngram );
			String[] gram = ngram.getGramArray();
			String[] nM1Gram = new String[n-1];
			for ( int i = 0; i < n-1 ; i++ )
				nM1Gram[i] = gram[i];
			double nM1C = (double) nM1GramCounts.get( new NGram( nM1Gram));
			double relFreq = nC/nM1C;
			probs.put(ngram, relFreq );
		}
		return probs;
	}	


	private  Map<NGram, Integer> count( Deque<String> valueQ, Map<NGram, Integer> counts, int n ){
		
		NGram gram = new NGram(valueQ, n);
		if ( counts.containsKey(gram)){
			int newC = counts.get(gram) +1;	 
			counts.put(gram, newC);
		} else {
			counts.put(gram, 1);
		}
		return counts;
	}

	private void setLongGrams( Iterable<Observation> data, int n ){}
	
	public Map<NGram,Integer> getNGramCounts( ){
		return nGramCounts;
	}
	
	public Map<NGram,Integer> getNM1GramCounts( ){
		return nM1GramCounts;
	}

	public Map<NGram,Double> getProbabilities( ){
		return nGramProbs;
	}

}

