package org.uva.predictions;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class NGrams{
	
	ValueType type;
	int n;
	
	Map<NGram,Integer> nGramCounts;
	Map<NGram,Integer> nM1GramCounts;
	Map<NGram,Double> nGramSmoothedCounts;
	Map<NGram,Double> nM1GramSmoothedCounts;
	
	Map<NGram, Double> nGramProbs;
	Map<NGram, Double> nGramProbsSmthd;
		
	/**
	 *
	 * assumes only 1 form is present
	 */
	public NGrams( Iterable<Observation> data, ValueType type, int n ){
		buildModel(data, n, type);
		this.type = type;
		this.n = n;
//		switch (type) {
//			case Question : nGramCounts = setStringGrams(data, n);
//			case TimeStamp : setLongGrams(data, n);
//		} 
	}

	private void buildModel( Iterable<Observation> data, int n, ValueType type ){
		nGramCounts = setGramCounts(data, n, type );
		nM1GramCounts = setGramCounts(data, n-1, type );
		nGramSmoothedCounts = gTsmoothe(nGramCounts);
		nM1GramSmoothedCounts = gTsmoothe(nM1GramCounts);
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

	private Map<NGram,Double> gTsmoothe( Map<NGram, Integer> counts ){
		int totalN = 0;
		int maxCount = 0;
		Map<Integer, Integer> countCounts = new HashMap<Integer, Integer>();
		for(Integer cnt : counts.values()){
			if( !countCounts.containsKey(cnt) )
				countCounts.put( cnt, 0 );
			countCounts.put(cnt, countCounts.get(cnt)+1);
			totalN += cnt;
			maxCount = (maxCount < cnt ? cnt : maxCount);
		}	
			System.out.printf( " max count = %d \n", maxCount);

		Map<NGram, Double> smoothedCounts = new HashMap<NGram, Double>();
		for (NGram gram : counts.keySet()){
			int cnt = counts.get(gram);
			int i = 1;
			double newCnt = -1;
			if ( cnt == maxCount ){
				newCnt = (double) countCounts.get(cnt);
			} else {
				while (!countCounts.containsKey(cnt+i))
					i++;
				newCnt = (cnt+1) * ( (double) countCounts.get(cnt+i)
													/ (double) countCounts.get(cnt));
			}
			smoothedCounts.put( gram, newCnt); 
		}
		int i = 1;
		while (!countCounts.containsKey(i))
			i++;
		smoothedCounts.put(new NGram("unseen", n, type), (double) countCounts.get(i)/(double) totalN);
		
		return smoothedCounts;
	}
	
	private  Map<NGram, Integer> count( Deque<String> valueQ, Map<NGram, Integer> counts ){
		
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

	public Map<NGram,Double> getNGramSmoothedCounts( ){
		return nGramSmoothedCounts;
	}
	
	public Map<NGram,Double> getNM1GramSmoothedCounts( ){
		return nM1GramSmoothedCounts;
	}

	public Map<NGram,Double> getProbabilitiesSMTHD( ){
		return nGramProbsSmthd;
	}


}

