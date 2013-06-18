import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.uva.predictions.*;
import org.uva.testing.*;


public class PredictorTester {
	public static void main(String[] args) throws InterruptedException 	{
		if(args.length < 2) {
			System.out.println("Syntax:\njava PredictorTester <in> <out>\n<in>: input file containing data\n<out>: results of the predictors");
			System.exit(1);
		}
		
		String dataLoc = args[0];
		String outputLoc = args[1];
		
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
		
		System.out.println("Writing results to " + outputLoc);
		
		try {
			ResultWriter writer = new ResultWriter(outputLoc);
			writer.write(result);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
