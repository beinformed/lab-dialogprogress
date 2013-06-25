package com.beinformed.research.labs.dialogprogress.testing;

import java.io.*;
import java.util.List;
import java.util.Random;

import com.beinformed.research.labs.dialogprogress.*;

public class PredictorTester {
	static List<Observation> test;
	static List<Observation> train;
	static boolean folds;
	static List<PredictorFactory> predictors;
	static String output;
	
	public static void main(String[] args) throws InterruptedException {

		parseArgs(args);
		TestFrame frame = new TestFrame(predictors);

		Iterable<TestResult> result;
		
		//List<Observation> testSet = train.subList(400, 507);
		//List<Observation> trainSet = train.subList(0, 399);
		
		if(folds)
			result = frame.testAllFolds(train, 4);
		else
			result = frame.testAll(train, test);

		try {
			ResultWriter writer = new ResultWriter(output);
			writer.write(result);
			writer.close();
			System.out.println("Written results to " + output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void parseArgs(String[] args) {
		if (args.length < 4)
			errorExit();
		
		if(args[0].equals("-f")) {
			folds = true;
			train = readDataFile(args[1]);
		} else {
			train = readDataFile(args[0]);
			test = readDataFile(args[1]);
		}
		
		output = args[2];
		predictors = readConfig(args[3]);
	}
	
	private static List<PredictorFactory> readConfig(String configLoc) {
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
		return predictors;
	}

	private static List<Observation> readDataFile(String dataLoc) {
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

		return reader.getObservations();
	}

	static void errorExit() {
		System.out.println("Syntax:\njava PredictorTester <train> <test> <out> <config>\n<train>: " + 
				"trainingset\n<test>:testset\n<out>: results of the predictors\n" + 
				"<config>: definition of what predictors to run");
		System.exit(1);
	}
}
