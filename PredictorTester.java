import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uva.predictions.*;
import org.uva.testing.*;


public class PredictorTester {
	public static void main(String[] args) throws InterruptedException 	{
		String dataLoc = "data/test2.csv";	
		
		List<Predictor> predictors = new ArrayList<Predictor>();
		predictors.add(new BaseLinePredictor(PredictionUnit.Time));
		predictors.add(new BaseLinePredictor(PredictionUnit.Steps));
		predictors.add(new PerObservationBLPredictor(PredictionUnit.Time));	
		predictors.add(new PerObservationBLPredictor(PredictionUnit.Steps));	
		predictors.add(new HighestConfidenceAggregator(PredictionUnit.Time));	
		predictors.add(new HighestConfidenceAggregator(PredictionUnit.Steps));	
		
		TestFrame frame = new TestFrame(predictors);
		List<Observation> data = new DataReader(dataLoc).getData();
		
		Iterable<TestResult> result = frame.testAll(data);
		
		for (TestResult r : result) {
			System.out.println(r.toString());
		}

	}
}
