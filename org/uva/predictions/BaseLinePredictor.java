

Class BaseLinePredictor implements Predictor {
	
	int nrQstns;
	double time;
	/**
 	 * Constructor BaseLinePredictor( int nrOfQuestions )
 	 * 
 	 * @param nrOfQuestions	total number of quetsions in form
 	 */ 
	public BaseLinePredictor( int nrOfQuestions ){
		nrQstns = nrOfQuestions;
	}

	/**
 	 * Method predict - takes the time taken to certain point
 	 * divides this time by answers given, then multiplicates with 
 	 * total questions.
 	 *
 	 * @param time		the time taken up to point to predict remaining time
 	 * @param qstnsAnswered	number of questions already answered
 	 * @return				remaining time
 	 */
	public double predict( double time, int qstnsAnswered ){
		double avgPerQ = time / qstnsAnswered;
		double remaining = ( nrQstns - qstnsAnswered ) * avgPerQ;
		return remaining;
	}
}

