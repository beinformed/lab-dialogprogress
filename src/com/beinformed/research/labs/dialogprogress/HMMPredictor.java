
package com.beinformed.research.labs.dialogprogress; 
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Opdf;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscrete;
import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;

public class HMMPredictor{ 

	double[] pi;
	List<String> states = new ArrayList<String>();	
	double[][] a;
	Hmm model;
	List<Opdf<ObservationDiscrete>> opdfs; 
	
	public HMMPredictor( Iterable<Observation> data){
		
		opdfs = new ArrayList<Opdf<ObservationDiscrete>>();
		setPi(data);	
		setTransitionProbs(data);
		model = new Hmm<ObservationDiscrete>(pi, a, opdfs);
	}	

	/**
 	 * Sets the chance of a state being an initial state
 	 */ 	
	private void setPi(Iterable<Observation> data){
		Map<String,Double> initialStates = new HashMap<String,Double>();
		double numberOObs = 0;
		for(Observation obs : data){
			setStates(obs);
			numberOObs += 1.0;
			String first = obs.getFirstAsked().getId();
			if (!initialStates.containsKey(first))
				initialStates.put(first, 1.0);
			else
				initialStates.put(first, initialStates.get(first)+1);
		}
		
		for( String key : initialStates.keySet() )
			initialStates.put(key, initialStates.get(key)/numberOObs );
	
		int i = 0;	
		pi = new double[states.size()];
		for( String state : states ){
			if( initialStates.containsKey(state) )
				pi[i] = initialStates.get(state);
			else
				pi[i] = 0;
			i++;
		}
	}

	private void setStates( Observation obs){
		for( Question q : obs.getQuestions() )
			if( !states.contains(q.getId()))
				states.add(q.getId());	
		opdfs.add(new OpdfDiscrete( HMMObservationVals.class ) );
	}

	private void setTransitionProbs( Iterable<Observation> data ){
		a = new double[states.size()][states.size()];
		int i = 0; int j = 0;
		NGrams nGramModel = new NGrams(data, EnumSet.of( ValueType.Question ) , 2);
		Map<NGram, Double> probs = nGramModel.getProbabilities();
		for(String stateI : states ){
			for( String stateJ : states ){
				String transStates = stateI + "," + stateJ;
				if(probs.containsValue(transStates))
					a[i][j] = probs.get(transStates);
				else
					a[i][j] = 0;
				j++;
			}
			i++;
		}
	}
} 
