package com.beinformed.research.labs.dialogprogress.testing;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;

import com.beinformed.research.labs.dialogprogress.*;
import com.beinformed.research.labs.dialogprogress.testing.*;

public class NGramsTester{
	
	public static void main( String[] args){
		
		String dataLoc = "data/testdata.csv";	
		List<Observation> data = new DataReader(dataLoc).getData();
		
		NGrams model = new NGrams(data, EnumSet.of(ValueType.Question), 3);
		Map<NGram, Double> probs = model.getProbabilities();
		Map<NGram, Integer> ngrams = model.getNGramCounts();
		Map<NGram, Integer> nM1grams = model.getNM1GramCounts();
		Map<NGram, Double> ngramsSmthd = model.getNGramSmoothedCounts();
		Map<NGram, Double> nM1gramsSmthd = model.getNM1GramSmoothedCounts();
		Map<NGram, Double> probsSmthd = model.getProbabilitiesSMTHD();

		System.out.printf("\n NGRAMS: \n");
		for ( NGram key : ngrams.keySet()){
			System.out.printf(" %s : %d  \n", key.toString(), ngrams.get(key) );
		}

		System.out.printf("\n N MIN 1 GRAMS: \n");
		for ( NGram key : nM1grams.keySet()){
			System.out.printf(" %s : %d  \n", key.toString(), nM1grams.get(key) );
		}

		System.out.printf("\n FREQUENCIES: \n");
		for ( NGram key : probs.keySet()){
			System.out.printf(" %s : %f  \n", key.toString(), probs.get(key) );
		}
		
		System.out.printf("\n SMOOTHED NGRAMS: \n");
		for ( NGram key : ngramsSmthd.keySet()){
			System.out.printf(" %s : %f  \n", key.toString(), ngramsSmthd.get(key) );
		}

		System.out.printf("\n SMOOTHED N MIN 1 GRAMS: \n");
		for ( NGram key : nM1gramsSmthd.keySet()){
			System.out.printf(" %s : %f  \n", key.toString(), nM1gramsSmthd.get(key) );
		}

		System.out.printf("\n SMOOTHED FREQUENCIES: \n");
		for ( NGram key : probsSmthd.keySet()){
			System.out.printf(" %s : %f  \n", key.toString(), probsSmthd.get(key) );
		}

	}
}


