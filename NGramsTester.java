import java.io.IOException;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import org.uva.predictions.*;
import org.uva.testing.*;

public class NGramsTester{
	
	public static void main( String[] args){
		
		String dataLoc = "data/testdata.csv";	
		List<Observation> data = new DataReader(dataLoc).getData();
		
		NGrams model = new NGrams(data, ValueType.Question, 3 );
		Map<NGram, Double> probs = model.getProbabilities();
		Map<NGram, Integer> ngrams = model.getNGramCounts();
		Map<NGram, Integer> nM1grams = model.getNM1GramCounts();

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
	}
}


