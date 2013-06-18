package com.beinformed.research.labs.dialogprogress.testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Graph {
	private String title;
	private String xLabel;
	private String yLabel;
	private List<Double> xValues;
	private List<Double> yValues;
	private String lineLabel;
	
	public Graph(String title, String xLabel, String yLabel, String lineLabel) {
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.lineLabel = lineLabel;
		
		this.xValues = new ArrayList<Double>();
		this.yValues = new ArrayList<Double>();
	}
	
	public void addDataPoint(double x, double y) {
		xValues.add(x);
		yValues.add(y);
	}
	
	public void writeCSV(FileWriter writer) throws IOException {		
		for(int i = 0; i < xValues.size(); i++) {
			writer.write(title + "," + 
					  xLabel + "," +
					  yLabel + "," +
					  lineLabel + "," +
					  xValues.get(i) + "," +
					  yValues.get(i) + "\n");
		}
	}
}
