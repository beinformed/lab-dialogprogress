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
		
		TestFrame frame = new TestFrame(predictors);
		List<Observation> data = new DataReader(dataLoc).getData();
		
		Iterable<TestResult> result = frame.testAll(data);
		
		System.out.println("Writing results to result.csv");
		
		try {
			ResultWriter writer = new ResultWriter("result.csv");
			writer.write(result);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
