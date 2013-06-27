
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

/**
 * This predictor initializes a HMM and trains it with the Baum-Welch algorithm. 
 * It requires the jahmm library to function. 
 *
 * A HMM is initialized with transition probabilities aquired by relative frequencies.
 * The hidden states use the status the form system returns at recieving data.  
 *
 * @see http://www.run.montefiore.ulg.ac.be/~francois/software/jahmm/
 */ 


public class HMMPredictor implements Predictor { 

	double[] pi;
	List<String> states = new ArrayList<String>();	
	double[][] a;
	Hmm<ObservationDiscrete<ReturnType>> model;
	ArrayList<OpdfDiscrete<ReturnType>> opdfs; 
	BaumWelchLearner trainer;
	List<List<ObservationDiscrete<ReturnType>>> sequences;
	private int nb;

	/**
     * @param	nb the number of iterations Baum-Welch performs
     */ 		
	public HMMPredictor(int nb){
		this.nb = nb;
	}	

	private void setPi(Iterable<Observation> data){
		Map<String,Double> initialStates = new HashMap<String,Double>();
		double numberOObs = 0;
		// sets all first questions in table
		// and sets all possible states since it iterates through the observations
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
			// opdfs is created here because it needs exact the same number
			// of arguments as pi
			opdfs.add( new OpdfDiscrete<ReturnType>( ReturnType.class ));
		}
	}

	private void setStates( Observation obs){
		List<ObservationDiscrete<ReturnType>> seq = new ArrayList<ObservationDiscrete<ReturnType>>();
		for( Question q : obs.getQuestions() ){
			if( !states.contains(q.getId()))
				states.add(q.getId());	
			seq.add( new ObservationDiscrete<ReturnType>( q.getStatus()));
		}
		if( seq.size() > 1 )
			sequences.add(seq);
	}

	private void setTransitionProbs( Iterable<Observation> data ){
	
		a = new double[states.size()][states.size()];
		
		// build ngram model to set relative frequencies
		// some extra work has to be done to use n>2-grams
		NGrams nGramModel = new NGrams(data, 2);
		Map<String, Double> probs = nGramModel.getProbabilities();
		int i = 0;
		for(String stateI : states ){
			int j = 0;
			for( String stateJ : states ){
				String transStates = stateI + "," + stateJ + ",";
				if(probs.containsKey(transStates))
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
		opdfs.trimToSize();

		if(model == null)
			model = new Hmm<ObservationDiscrete<ReturnType>>(pi, a, opdfs);
		trainer = new BaumWelchLearner();
		trainer.setNbIterations(nb);
		model = trainer.learn(model, sequences);
		
	}
	
	public Prediction predict ( Observation obs ){
		List<ObservationDiscrete<ReturnType>> seqTest = new ArrayList<ObservationDiscrete<ReturnType>>();
		for (Question q : obs.getQuestions()){
			seqTest.add( new ObservationDiscrete<ReturnType>( q.getStatus()));
		}
		
		int[] res = model.mostLikelyStateSequence(seqTest);
		int remSteps = res.length - obs.getNoQuestions();
		
		Prediction predSteps = new Prediction(.5, remSteps, remSteps, PredictionUnit.Steps );		
		return predSteps;
	}
	
	@Override
	public String toString() {
		return "Hidden Markov [" + nb + "]";
	}

} 
