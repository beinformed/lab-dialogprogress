import java.util.ArrayList;
import java.util.List;

import org.uva.predictions.*;
import org.uva.testing.*;


public class PredictorTester {
	public static void main(String[] args) 	{
		String dataLoc = "data/testdata.csv";
		
		List<Predictor> predictors = new ArrayList<Predictor>();
		predictors.add(new BaseLinePredictor(PredictionUnit.Time));
		predictors.add(new BaseLinePredictor(PredictionUnit.Steps));
		
		TestFrame frame = new TestFrame(predictors);
		List<Observation> data = new Reader(dataLoc).getData();
		
		Iterable<TestResult> result = frame.testAll(data);
		WriteCSV.write(result);
		
		for (TestResult r : result) {
			System.out.println(r.toString());
		}
		System.out.println(System.currentTimeMillis());
	}
}
