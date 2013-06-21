package com.beinformed.research.labs.dialogprogress.testing;

import java.io.*;
import java.util.List;

import com.beinformed.research.labs.dialogprogress.*;

public class PredictorTester {
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 3) {
			System.out.println("Syntax:\njava PredictorTester <in> <out>\n<in>: " + 
					"input file containing data\n<out>: results of the predictors" + 
					"<config>: definition of what predictors to run");
			System.exit(1);
		}

		String dataLoc = args[0];
		String outputLoc = args[1];
		String configLoc = args[2];

		List<PredictorFactory> predictors = null;
		ConfigReader configReader;
		try {
			configReader = new ConfigReader(configLoc);
			predictors = configReader.read();
			configReader.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

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
			System.out.println("Written results to " + outputLoc);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
