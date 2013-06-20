package com.beinformed.research.labs.dialogprogress.testing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.beinformed.research.labs.dialogprogress.*;

public class PredictorTester {
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.out
					.println("Syntax:\njava PredictorTester <in> <out>\n<in>: input file containing data\n<out>: results of the predictors");
			System.exit(1);
		}

		String dataLoc = args[0];
		String outputLoc = args[1];

		List<Predictor> predictors = new ArrayList<Predictor>();
		predictors.add(new AverageTreePredictor(PredictionUnit.Time));
		predictors.add(new AverageTreePredictor(PredictionUnit.Steps));
		predictors.add(new PerObservationBLPredictor(PredictionUnit.Time));
		predictors.add(new PerObservationBLPredictor(PredictionUnit.Steps));

		TestFrame frame = new TestFrame(predictors);

		DataReader reader = null;
		try {
			reader = new DataReader(dataLoc);
			reader.read();
			reader.close();
		} catch (FileNotFoundException e1) {
			System.err.println("File " + dataLoc + " was not found.");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		List<Observation> data = reader.getObservations();

		Iterable<TestResult> result = frame.testAll(data);

		try {
			ResultWriter writer = new ResultWriter(outputLoc);
			writer.write(result);
			writer.close();
			System.out.println("Written results to " + outputLoc + ". Generating graph...");
			
			Process python = new ProcessBuilder("python", "src/plot.py", outputLoc, "results/graph.png")
					.start();

			String line;

			BufferedReader input = new BufferedReader(new InputStreamReader(
					python.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			System.out.println("Graph created");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
