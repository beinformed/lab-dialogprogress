package com.beinformed.research.labs.dialogprogress.testing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beinformed.research.labs.dialogprogress.AverageTreePredictor;
import com.beinformed.research.labs.dialogprogress.NeuralNetworkPredictor;
import com.beinformed.research.labs.dialogprogress.PerObservationBLPredictor;
import com.beinformed.research.labs.dialogprogress.PredictionUnit;
import com.beinformed.research.labs.dialogprogress.Predictor;

public class ConfigReader {

	private BufferedReader reader;

	public ConfigReader(String location) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(location));
	}
	public void close() throws IOException {
		if (reader != null)
			reader.close();
	}
	
	public List<PredictorFactory> read() throws IOException {
		List<PredictorFactory> factories = new ArrayList<PredictorFactory>();
		String line = reader.readLine();
		
		while(line != null && !line.startsWith("#")) {
			String[] parts = line.split("=");
			String pred = parts[0];
			String args = parts.length > 1 ? parts[1] : "";
			
			factories.add(getFactory(PredictionUnit.Time, pred, args));
			factories.add(getFactory(PredictionUnit.Steps, pred, args));
			line = reader.readLine();
		}
		
		return factories;
	}
	private PredictorFactory getFactory(PredictionUnit unit, String pred, String args) {
		switch(pred) {
			case "NeuralNetworkPredictor":
				return getNNFactory(unit, args);
			case "AverageTreePredictor":
				return getAverageTreeFactory(unit, args);
			default:
				return getBaseLineFactory(unit);
		}
	}
	private PredictorFactory getBaseLineFactory(final PredictionUnit unit) {
		return new PredictorFactory () {
			@Override
			public Predictor getPredictor() {
				return new PerObservationBLPredictor(unit);
			} };
	}
	private PredictorFactory getAverageTreeFactory(final PredictionUnit unit, String args) {
		final double[] values = splitArgs(args);
		return new PredictorFactory() {

			@Override
			public Predictor getPredictor() {
				return new AverageTreePredictor(unit, values[0], values[1], values[2]);
			} };
	}
	private PredictorFactory getNNFactory(final PredictionUnit unit, String args) {
		final double[] values = splitArgs(args);
		final int[] layers = new int[values.length - 1];
		for(int i = 1; i < values.length; i++) {
			layers[i-1] = (int) values[i];
		}
		
		return new PredictorFactory() {

			@Override
			public Predictor getPredictor() {
				return new NeuralNetworkPredictor(unit, (int) values[0], layers);
			} };
	}
	
	private double[] splitArgs(String args) {
		String[] parts = args.split("|");
		double[] values = new double[parts.length];
		for(int i = 0; i < parts.length; i++)
			values[i] = Double.parseDouble(parts[i]);
		return values;
	}
}
