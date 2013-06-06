import java.util.ArrayList;
import java.util.List;

import org.uva.predictions.*;


public class PredictorTester {
	public static void main(String[] args) 	{
		List<Predictor> predictors = new ArrayList<Predictor>();
		predictors.add(new BaseLinePredictor());
		
		TestFrame frame = new TestFrame(predictors);
		List<Observation> data = new Reader().getData("test.csv", "forms.csv");
		
		Iterable<TestResult> result = frame.testAll(data);
		
		for (TestResult r : result) {
			System.out.println(r.toString());
		}
	}
}
