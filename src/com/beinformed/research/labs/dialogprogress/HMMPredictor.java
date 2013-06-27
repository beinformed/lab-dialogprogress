
package com.beinformed.research.labs.dialogprogress; 
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
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
	Hmm<ObservationDiscrete<ReturnType>> model;
	List<OpdfDiscrete<ReturnType>> opdfs; 
	BaumWelchLearner trainer;
	List<List<ObservationDiscrete<ReturnType>>> sequences;
	
	public HMMPredictor( ){
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
		List<ObservationDiscrete<ReturnType>> seq = new ArrayList<ObservationDiscrete<ReturnType>>();
		for( Question q : obs.getQuestions() ){
			if( !states.contains(q.getId()))
				states.add(q.getId());	
			seq.add( new ObservationDiscrete<ReturnType>( q.getRType()));
		}
		sequences.add(seq);
		opdfs.add(new OpdfDiscrete<ReturnType>( ReturnType.class ) );
	}

	private void setTransitionProbs( Iterable<Observation> data ){
	
		a = new double[states.size()][states.size()];
		NGrams nGramModel = new NGrams(data, 2);
		Map<String, Double> probs = nGramModel.getProbabilities();
		int i = 0;
		for(String stateI : states ){
			int j = 0;
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
	
	
	public void train(Iterable<Observation> data ){
		
		opdfs = new ArrayList<OpdfDiscrete<ReturnType>>();
		sequences = new ArrayList<List<ObservationDiscrete<ReturnType>>>();
		setPi(data);	
		setTransitionProbs(data);
		model = new Hmm<ObservationDiscrete<ReturnType>>(pi, a, opdfs);
		trainer = new BaumWelchLearner();
		trainer.learn(model, sequences);
	}
} 
