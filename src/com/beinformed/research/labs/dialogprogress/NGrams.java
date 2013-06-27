package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class NGrams{
	
	private int n;
//	private EnumSet<ValueType> type;	
	private Map<String,Integer> nGramCounts;
	private Map<String,Integer> nM1GramCounts;
//	private Map<NGram,Double> nGramSmoothedCounts;
//	private Map<NGram,Double> nM1GramSmoothedCounts;
	
	private Map<String, Double> nGramProbs;
//	private Map<NGram, Double> nGramProbsSmthd;
		
	/**
	 *
	 * assumes only 1 form is present
	 */
	public NGrams( Iterable<Observation> data, int n ){
//		this.type = type;
		this.n = n;
		buildModel(data);
	}

	private void buildModel( Iterable<Observation> data ){
		nGramCounts = findGramCounts(data, n );
		nM1GramCounts = findGramCounts(data, n-1 );
		//nGramSmoothedCounts = gTsmoothe(nGramCounts);
		//nM1GramSmoothedCounts = gTsmoothe(nM1GramCounts);
		nGramProbs = calcProbabilities(n);
	}

	private Map<String, Integer> findGramCounts( Iterable<Observation> data, int n ){
//		Question start = new Question("/s", "null", 0, "DATA_MISSING");
//		Question end = new Question("/e", "null", 0, "OK");
		
		String start = "/s";
		String end = "/e";		
		Map<String, Integer> counts = new HashMap<String, Integer>();
		for (Observation obs : data){
			ArrayDeque<String> valueQ = new ArrayDeque<String>();
			valueQ.offerFirst(start);
			for ( Question q : obs.getQuestions()  ){
				if (valueQ.size() == n){
					counts = count(valueQ, counts, n);
					valueQ.pollFirst();
				}
				valueQ.offerLast( q.getId() + " ");
				counts = count(valueQ, counts, n);
				valueQ.pollFirst();
			}
			valueQ.offerLast(end);
			counts = count(valueQ, counts, n);
		}
		return counts;
	}

	private Map<String, Double> calcProbabilities(int n){
		Map<String, Double> probs = new HashMap<String, Double>();
		for( String ngram : nGramCounts.keySet() ){
			double nC = (double) nGramCounts.get( ngram );
			double nM1C = (double) nM1GramCounts.get( getParent(ngram));
			double relFreq = nC/nM1C;
			probs.put(ngram, relFreq );
		}
		return probs;
	}	
	
	private  Map<String, Integer> count( Deque<String> valueQ, Map<String, Integer> counts, int n ){
		
		String gram = "";
		for ( String s : valueQ )
			gram += s + ",";
		
		if ( counts.containsKey(gram)){
			int newC = counts.get(gram) +1;	 
			counts.put(gram, newC);
		} else {
			counts.put(gram, 1);
		}
		return counts;
	}

	private String getParent( String ngram ){
		String[] par = ngram.split(",");
		
		if(par.length < 1)
			return ngram.substring(0, ngram.length() - 1);
		
		String parent = "";
		int i = 0;
		while ( i < n-1 )
			parent += par[i++] + ",";
		return parent;
	}
/**
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
			if ( cnt < 6  ){
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
		//smoothedCounts.put(new NGram("unseen", n, type), (double) countCounts.get(i)/(double) totalN);
		
		return smoothedCounts;
	}
**/
	public Map<String,Integer> getNGramCounts( ){
		return nGramCounts;
	}
	
	public Map<String,Integer> getNM1GramCounts( ){
		return nM1GramCounts;
	}

	public Map<String,Double> getProbabilities( ){
		return nGramProbs;
	}
/**
	public Map<String,Double> getNGramSmoothedCounts( ){
		return nGramSmoothedCounts;
	}
	
	public Map<String,Double> getNM1GramSmoothedCounts( ){
		return nM1GramSmoothedCounts;
	}

	public Map<String,Double> getProbabilitiesSMTHD( ){
		return nGramProbsSmthd;
	}
**/

}

