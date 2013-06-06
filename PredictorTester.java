import java.util.ArrayList;
import java.util.List;

import org.uva.predictions.*;
import org.uva.testing.*;


public class PredictorTester {
	public static void main(String[] args) 	{
		String dataLoc = "data/testdata.csv";
		String formLoc = "data/testform.csv";
		
		List<Predictor> predictors = new ArrayList<Predictor>();
		predictors.add(new BaseLinePredictor());
		
		TestFrame frame = new TestFrame(predictors);
		List<Observation> data = new Reader(dataLoc, formLoc).getData();
		
		Iterable<TestResult> result = frame.testAll(data);
		
		for (TestResult r : result) {
			System.out.println(r.toString());
		}
	}
}
