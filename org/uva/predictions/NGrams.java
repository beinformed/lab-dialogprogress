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

	private void buildModel( Iterable<Observation> data, int n, ValueType type ){
		nGramCounts = setGramCounts(data, n, type );
		nM1GramCounts = setGramCountss(data, n-1, type );
		nGramProbs = calcProbabilities(n);
	}

	private Map<NGram, Integer> setGramCounts( Iterable<Observation> data, int n, ValueType type ){
	
		Map<NGram, Integer> counts = new HashMap<NGram, Integer>();
		for (Observation obs : data){
			int i = 1;	
			ArrayDeque<String> valueQ = new ArrayDeque<String>();
			valueQ.offerFirst("/s");
			for ( Question q : obs.getQuestions()  ){
				valueQ.offerLast( q.getId() );
				if (++i >= n){
					counts = count(valueQ, counts, n, type);
					String s = valueQ.pollFirst();
				}
			}
			valueQ.offerLast("/e");
			counts = count(valueQ, counts, n, type);
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


	private  Map<NGram, Integer> count( Deque<String> valueQ, Map<NGram, Integer> counts, int n, ValueType type ){
		
		NGram gram = new NGram(valueQ, n, type);
		if ( counts.containsKey(gram)){
			int newC = counts.get(gram) +1;	 
			counts.put(gram, newC);
		} else {
			counts.put(gram, 1);
		}
		return counts;
	}

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

